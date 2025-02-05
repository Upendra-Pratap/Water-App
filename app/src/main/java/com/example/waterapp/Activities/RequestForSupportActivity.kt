package com.example.waterapp.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waterapp.Fragment.AccountFragment
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityRequestForSupportBinding
import com.example.waterapp.updateAddressModel.UpdateAddressViewModel
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

@AndroidEntryPoint
class RequestForSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestForSupportBinding
    private val updateAddressViewModel: UpdateAddressViewModel by viewModels()
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    private var userId =""
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var sharedPreferences: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestForSupportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = CustomProgressDialog(this)

        //observer
        updateAddressObserver()

        binding.imageUploadBtn.setOnClickListener { requestCameraPermission() }

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId= sharedPreferences.getString("userId", userId).toString().trim()

        binding.backArrow.setOnClickListener { finish() }

        binding.forgotbutton.setOnClickListener {
            formValidation()
        }
    }

    private fun updateAddressObserver() {
        updateAddressViewModel.progressIndicator.observe(this){

        }
        updateAddressViewModel.mRejectResponse.observe(this){
            val success = it.peekContent().success
            val message = it.peekContent().message
            if (success == true){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RequestForSupportActivity, AccountFragment::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        updateAddressViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@RequestForSupportActivity, it)
        }
    }

    private fun validationInputs(street: String, city: String, pin: String): Boolean {
        return when{
            street.isEmpty()->{
                binding.emailtext.error = getString(R.string.error_user_street)
                false
            }
            city.isEmpty()->{
                binding.datepickertext.error = getString(R.string.error_user_city)
                false
            }
            pin.isEmpty()->{
                binding.streettext.error = getString(R.string.error_user_pin)
                false
            }
            else-> true
        }
    }

    private fun formValidation() {
        val street = binding.emailtext.text.toString().trim()
        val city = binding.datepickertext.text.toString().trim()
        val pin = binding.streettext.text.toString().trim()

        if (validationInputs(street, city, pin)){
            updateAddressApi(userId)
        }
    }

    private fun updateAddressApi(userId: String) {
        val street = binding.emailtext.text.toString().trim()
        val city = binding.datepickertext.text.toString().trim()
        val pin = binding.streettext.text.toString().trim()


        val streetBody = RequestBody.create("text/plain".toMediaTypeOrNull(), street)
        val cityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), city)
        val pinBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pin)


        val imagePart = selectedImageFile?.let {
            val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("id_proof", it.name, requestBody)
        }

        if (imagePart != null) {
            this?.let {
                updateAddressViewModel.updateAddress(
                    userId,
                    streetBody,
                    cityBody,
                    pinBody,
                    imagePart,

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
            Toast.makeText(this, "Error converting URI to file", Toast.LENGTH_SHORT).show()
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