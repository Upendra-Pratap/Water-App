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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityGenerateReportBinding
import com.example.waterapp.generateReportModel.GenerateReportViewModel
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
    private lateinit var globalSpinner: Spinner
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: Activity
    private lateinit var progressDialog: CustomProgressDialog
    private var userId =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenerateReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBack.setOnClickListener { finish() }

        progressDialog = CustomProgressDialog(this)
        activity = this

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        globalSpinner = binding.coursesspinner

        val coursesList = listOf(
            "Problem Types",
            "Fallen Pole",
            "Damaged Cable",
            "Power Outage",
            "Water Leak",
            "Others")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            coursesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        globalSpinner.adapter = adapter

        globalSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle when no item is selected (optional)
                }
            }
        binding.forgotbutton.setOnClickListener {
            formVelidation()
        }

        binding.datepicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    binding.datepickertext.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }

        binding.datepickertext.setOnClickListener {
            binding.datepicker.performClick()
        }

        binding.imageUploadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                requestCameraPermission()
            }
        }
    }

    private fun velidationInput(
        description: String,
        date: String,
        street: String,
        city: String,
        pinCode: String
    ): Boolean {
        return when {
            description.isEmpty() -> {
                binding.emailtext.error = "Please Enter Description"
                false
            }

            date.isEmpty() -> {
                binding.datepickertext.error = "Please Enter Date"
                false
            }

            street.isEmpty() -> {
                binding.streettext.error = "Please Enter Street Line"
                false
            }

            city.isEmpty() -> {
                binding.citytext.error = "Please Enter Your City"
                false
            }

            pinCode.isEmpty() -> {
                binding.pinCodeText.error = "Please Enter Your Pin Code"
                false
            }
            else -> true
        }
    }

    private fun formVelidation() {
        val description = binding.emailtext.text.toString().trim()
        val date = binding.datepickertext.text.toString().trim()
        val street = binding.streettext.text.toString().trim()
        val city = binding.citytext.text.toString().trim()
        val pinCode = binding.pinCodeText.text.toString().trim()

        if (velidationInput(description, date, street, city, pinCode)) {
            //calling api  here
            generateReportApi(userId, globalSpinner, description, selectedDate, street, city, pinCode)
        }
    }

    private fun generateReportApi(
        id: String,
        coursesList: Spinner,
        description: String,
        selectedDate: String,
        street: String,
        city: String,
        pinCode: String
    ) {
        val selectedCourse = coursesList.selectedItem.toString()

        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
        val globalSpinner = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedCourse)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val selectedDateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), selectedDate)
        val streetBody = RequestBody.create("text/plain".toMediaTypeOrNull(), street)
        val cityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), city)
        val pinCodeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pinCode)

        // Handle the image file and convert it into a MultipartBody.Part
        val imageFile = selectedImageFile
        val imagePart = imageFile?.let {
            val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestBody)
        }

        // Call generateReportViewModel with all the necessary parameters, including the image part
        if (imagePart != null) {
            generateReportViewModel.generateReport(
                userId,
                globalSpinner,
                descriptionBody,
                selectedDateBody,
                streetBody,
                cityBody,
                pinCodeBody,
                imagePart
            )
        } else {
            // If there's no image, call generateReport without the image part
            if (imagePart != null) {
                generateReportViewModel.generateReport(
                    userId,
                    globalSpinner,
                    descriptionBody,
                    selectedDateBody,
                    streetBody,
                    cityBody,
                    pinCodeBody,
                    imagePart
                )
            }
        }
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
            } else {
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun bitmapToFile(bitmap: Bitmap): File {
        val file = File(this.cacheDir, "image.jpg")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
        }
        return file
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
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
    @SuppressLint("IntentReset")
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        selectImageLauncher.launch(galleryIntent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(this.packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertUriToFile(uri: Uri): File? {
        return try {
            val inputStream: InputStream? = this.contentResolver.openInputStream(uri)
            inputStream?.let {
                val fileName = getFileNameFromUri(uri)
                val outputFile = File.createTempFile("image", ".jpg", this.cacheDir)
                FileOutputStream(outputFile).use { output ->
                    inputStream.use { input ->
                        input.copyTo(output)
                    }
                }
                outputFile
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Error converting URI to File", Toast.LENGTH_SHORT).show()
            Log.e("ProfileFragment", "Error converting URI to File: ${e.message}", e)
            null
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        this.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = cursor.getString(displayNameIndex)
                }
            }
        }
        return result ?: "file"
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


