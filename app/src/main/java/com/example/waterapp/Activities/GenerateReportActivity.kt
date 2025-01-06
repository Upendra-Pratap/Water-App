package com.example.waterapp.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waterapp.Dashboard.DashboardActivity
import com.example.waterapp.Fragment.AccountFragment
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityGenerateReportBinding
import com.example.waterapp.generateReportModel.GenerateReportViewModel
import com.example.waterapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar

@AndroidEntryPoint
class GenerateReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateReportBinding
    private val generateReportViewModel: GenerateReportViewModel by viewModels()
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    private var selectedDate = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: CustomProgressDialog
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenerateReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = CustomProgressDialog(this)
        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "").toString().trim()

        setupUI()
        generateReportObserver()
    }

    private fun setupUI() {
        binding.arrowBack.setOnClickListener { finish() }

        val coursesList = listOf("Problem Types", "Fallen Pole", "Damaged Cable", "Power Outage", "Water Leak", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, coursesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.coursesspinner.adapter = adapter

        binding.forgotbutton.setOnClickListener {
            formValidation()
        }

        binding.datepicker.setOnClickListener { showDatePicker() }
        binding.datepickertext.setOnClickListener { binding.datepicker.performClick() }

        binding.imageUploadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                requestCameraPermission()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.datepickertext.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun generateReportObserver() {
        generateReportViewModel.progressIndicator.observe(this) { isLoading ->
            if (isLoading) progressDialog.start()
            else progressDialog.stop()
        }

        generateReportViewModel.mRejectResponse.observe(this) {
            val message = it.peekContent().message
            val status = it.peekContent().success

            if (status == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@GenerateReportActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        generateReportViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun formValidation() {
        val description = binding.emailtext.text.toString().trim()
        val date = binding.datepickertext.text.toString().trim()
        val street = binding.streettext.text.toString().trim()
        val city = binding.citytext.text.toString().trim()
        val pinCode = binding.pinCodeText.text.toString().trim()

        if (validateFields(description, date, street, city, pinCode)) {
            generateReportApi(description, date, street, city, pinCode)
        }
    }

    private fun validateFields(description: String, date: String, street: String, city: String, pinCode: String): Boolean {
        var isValid = true

        if (description.isEmpty()) {
            binding.emailtext.error = "Please Enter Description"
            isValid = false
        }
        if (date.isEmpty()) {
            binding.datepickertext.error = "Please Enter Date"
            isValid = false
        }
        if (street.isEmpty()) {
            binding.streettext.error = "Please Enter Street Line"
            isValid = false
        }
        if (city.isEmpty()) {
            binding.citytext.error = "Please Enter Your City"
            isValid = false
        }
        if (pinCode.isEmpty()) {
            binding.pinCodeText.error = "Please Enter Your Pin Code"
            isValid = false
        }

        return isValid
    }

    private fun generateReportApi(description: String, selectedDate: String, street: String, city: String, pinCode: String) {
        val selectedCourse = binding.coursesspinner.selectedItem.toString()
        val requestBodyMap = mapOf(
            "course" to selectedCourse,
            "description" to description,
            "date" to selectedDate,
            "street" to street,
            "city" to city,
            "pinCode" to pinCode
        ).mapValues { (key, value) -> RequestBody.create("text/plain".toMediaTypeOrNull(), value) }

        val imagePart = selectedImageFile?.let {
            val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("reportImages", it.name, requestBody)
        }

        imagePart?.let {
            generateReportViewModel.generateReport(
                userId, requestBodyMap["course"]!!, requestBodyMap["description"]!!,
                requestBodyMap["date"]!!, requestBodyMap["street"]!!, requestBodyMap["city"]!!,
                requestBodyMap["pinCode"]!!, it
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            selectImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun selectImage() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(this).apply {
            setTitle("Select Image")
            setItems(options) { dialog, item ->
                when (options[item]) {
                    "Take Photo" -> openCamera()
                    "Choose from Gallery" -> openGallery()
                    "Cancel" -> dialog.dismiss()
                }
            }
            show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        selectImageLauncher.launch(galleryIntent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertUriToFile(uri: Uri): File? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.let {
                val fileName = getFileNameFromUri(uri)
                val outputFile = File.createTempFile("image", ".jpg", cacheDir)
                FileOutputStream(outputFile).use { output ->
                    inputStream.use { input -> input.copyTo(output) }
                }
                outputFile
            }
        } catch (e: IOException) {
            Log.e("GenerateReportActivity", "Error converting URI to File", e)
            null
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return result ?: "file"
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    selectedImageFile = bitmapToFile(it)
                    binding.submitImage.setImageBitmap(it)
                } ?: Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(cacheDir, "image.jpg")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        }
        return file
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageFile = convertUriToFile(uri)
                    selectedImageFile?.let { file ->
                        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(file))
                        binding.submitImage.setImageBitmap(bitmap)
                    } ?: Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }

}
