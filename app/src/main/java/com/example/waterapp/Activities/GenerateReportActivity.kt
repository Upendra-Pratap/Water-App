package com.example.waterapp.Activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.example.waterapp.databinding.ActivityGenerateReportBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar

class GenerateReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenerateReportBinding
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenerateReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBack.setOnClickListener { finish() }

        binding.datepicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
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

