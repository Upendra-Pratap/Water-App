package com.example.waterapp.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.waterapp.R
import com.example.waterapp.SignUpModel.SignUpResponse
import com.example.waterapp.SignUpModel.SignUpViewModel
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private  val signUpViewModel: SignUpViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupObservers()

        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.sendOtp.setOnClickListener {
            formVelidation()
        }
        binding.chooseImage.setOnClickListener{
            requestCameraPermission()
        }
        binding.arrowBack.setOnClickListener { finish() }
    }

    private fun velidationInputs(
        userName: String,
        userEmail: String,
        userPhone: String,
        password : String,
        city : String,
        street : String,
        pin : String
    ): Boolean {
        return when{
            userName.isEmpty()->{
                binding.userName.error = "Please enter user name"
                false
            }
            userEmail.isEmpty()->{
                binding.lastName.error = "Please enter your email"
                false
            }
            userPhone.isEmpty()->{
                binding.passwordTxt.error = "Please enter your phone Number"
                false
            }
            password.isEmpty()->{
                binding.passwordName.error = "Please enter your password"
                false
            }
            city.isEmpty()->{
                binding.cityName.error = "Please enter your city"
                false
            }
            street.isEmpty()->{
                binding.streetName.error = "Please enter your street"
                false
            }
            pin.isEmpty()->{
                binding.pinName.error = "Please enter your pin"
                false
            }
            else -> true
        }
    }

    private fun formVelidation() {
        val userName = binding.userName.text.toString()
        val userEmail = binding.lastName.text.toString()
        val userPhone = binding.passwordTxt.text.toString()
        val password = binding.passwordName.text.toString()
        val city = binding.cityName.text.toString()
        val street = binding.streetName.text.toString()
        val pin = binding.pinName.text.toString()

        if (velidationInputs(userName, userEmail, userPhone, password, city, street, pin)){
            signUpApi()
        }
    }

    private fun signUpApi() {
        val userName = binding.userName.text.toString()
        val userEmail = binding.lastName.text.toString()
        val userPhone = binding.passwordTxt.text.toString()
        val password = binding.passwordName.text.toString()
        val city = binding.cityName.text.toString()
        val street = binding.streetName.text.toString()
        val pin = binding.pinName.text.toString()

        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userName)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userEmail)
        val phoneNoBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userPhone)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val streetBody = RequestBody.create("text/plain".toMediaTypeOrNull(), street)
        val cityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), city)
        val pinBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pin)

        val imageFile = selectedImageFile
        val imagePart = imageFile?.let {
            val requestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestBody)
        }

        if (imagePart != null) {
            signUpViewModel.customerLogin(nameBody, emailBody, phoneNoBody,passwordBody,streetBody,cityBody,pinBody, imagePart)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupObservers() {
        signUpViewModel.progressIndicator.observe(this, Observer { show ->
            if (show) progressDialog.start(getString(R.string.please_wait)) else progressDialog.stop()
        })

        signUpViewModel.errorResponse.observe(this, Observer { error ->
            showToast(error.message ?: "An unexpected error occurred")

            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()

        })

        signUpViewModel.mRejectResponse.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->


                showToast(response.message ?: "Registration successful")

                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()

                handleRegistrationResponse(response)
            }
        })
    }

    private fun handleRegistrationResponse(response: SignUpResponse) {
        response.message?.let {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                selectedImageFile = bitmapToFile(it)
                binding.profileImg.setImageBitmap(it)
            } ?:
            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
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
                FileOutputStream(outputFile).use { output -> inputStream.use { input ->
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
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageFile = convertUriToFile(uri)
                selectedImageFile?.let { file ->
                    val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(file))
                    binding.profileImg.setImageBitmap(bitmap)
                } ?:
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
