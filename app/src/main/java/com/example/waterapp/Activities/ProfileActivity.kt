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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.waterapp.databinding.ActivityProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        binding.chooseImage.setOnClickListener {
            requestCameraPermission()
        }
        binding.updateHere.setOnClickListener {
            profilVelidation()
        }
    }
    private fun VelidationInputs(
        userName: String,
        email: String,
        phoneNumber: String,
        address: String
    ): Boolean {
        return when{
            userName.isEmpty()->{
                binding.userName.error = "Please Enter Your Name"
                false
            }

            email.isEmpty()->{
                binding.email.error = "Please Enter Your Email"
                false
            }

            phoneNumber.isEmpty()-> {
                binding.phoneNumber.error = "Please Enter Your Phone Number"
                false

            }

            address.isEmpty()-> {
                binding.addressText.error = "Please Enter Your Phone Number"
                false

            }
            else-> true
        }
    }

    private fun profilVelidation() {
        val userName = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        val address = binding.addressText.text.toString().trim()

        if (VelidationInputs(userName, email, phoneNumber, address)){
            //calling Api here
            updateProfileApi()
        }
    }
    private fun updateProfileApi() {
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        startActivity(intent)
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