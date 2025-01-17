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
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.waterapp.BuildConfig
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.databinding.ActivityProfileBinding
import com.example.waterapp.updateProfileModel.GetUpdateProfileViewModel
import com.example.waterapp.updateProfileModel.UpdateProfileResponse
import com.example.waterapp.updateProfileModel.UpdateProfileViewModel
import com.example.waterapp.utils.ErrorUtil
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
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var selectedImageFile: File? = null
    private val CAMERA_PERMISSION_CODE = 101
    private val updateProfileViewModel: UpdateProfileViewModel by viewModels()
    private val getUpdateProfileViewModel: GetUpdateProfileViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBack.setOnClickListener { finish() }

        setupObservers()

        sharedPreferences = applicationContext.getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", userId).toString().trim()

        //Observer and Api
        getUpdateProfileApi(userId)
        getUpdateProfileObserver()

        binding.chooseImage.setOnClickListener { requestCameraPermission() }
        binding.updateHere.setOnClickListener { profileValidation() }
    }

    private fun getUpdateProfileObserver() {
        getUpdateProfileViewModel.progressIndicator.observe(this) {

        }
        getUpdateProfileViewModel.mCustomerResponse.observe(this) {
            val status = it.peekContent().success
            val userData = it.peekContent().data
            val userAddress = it.peekContent().data?.address

            if (status == true) {
                if (userData?.userName == null) {

                } else {
                    binding.userName.text =
                        Editable.Factory.getInstance().newEditable(userData.userName.toString())
                }
                if (userData?.userEmail == null) {

                } else {
                    binding.email.text =
                        Editable.Factory.getInstance().newEditable(userData.userEmail.toString())
                }
                if (userData?.phoneNo == null) {

                } else {
                    binding.phoneNumber.text =
                        Editable.Factory.getInstance().newEditable(userData.phoneNo.toString())
                }
                if (userAddress?.city == null) {

                } else {
                    binding.cityText.text =
                        Editable.Factory.getInstance().newEditable(userAddress.city.toString())

                }
                if (userAddress?.street == null) {

                } else {
                    binding.streetText.text =
                        Editable.Factory.getInstance().newEditable(userAddress.street.toString())

                }
                if (userAddress?.zip == null) {

                } else {
                    binding.pinText.text =
                        Editable.Factory.getInstance().newEditable(userAddress.zip.toString())
                }
                if (userData?.profileImage == null) {

                } else {
                    val url = it.peekContent().data?.profileImage
                    Glide.with(this).load(BuildConfig.IMAGE_KEY + url)
                        .placeholder(R.drawable.electricity).error(R.drawable.water)
                        .into(binding.profileImg)


                }
            }
        }
        getUpdateProfileViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ProfileActivity, it)
        }
    }

    private fun getUpdateProfileApi(userId: String) {

        getUpdateProfileViewModel.getUpdateProfile(userId, progressDialog, this)
    }

    private fun setupObservers() {
        updateProfileViewModel.progressIndicator.observe(this) { show ->
            if (show) progressDialog.start(getString(R.string.please_wait)) else progressDialog.stop()
        }

        updateProfileViewModel.errorResponse.observe(this) { error ->
            showToast(error.message ?: getString(R.string.unexpected_error))
        }

        updateProfileViewModel.mRejectResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                showToast(response.message ?: getString(R.string.updated_profile))
                handleProfileUpdateResponse(response)
            }
        }
    }

    private fun handleProfileUpdateResponse(response: UpdateProfileResponse) {
        response.Data()?.let {
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            //getProfileApi(customerId)

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun ValidationInputs(
        userName: String,
        email: String,
        phoneNumber: String,
        address: String,
        city: String,
        street: String,
        pin: String,
    ): Boolean {
        return when {
            userName.isEmpty() -> {
                binding.userName.error = getString(R.string.error_user_name)
                false
            }

            email.isEmpty() -> {
                binding.email.error = getString(R.string.error_user_email)
                false
            }

            phoneNumber.isEmpty() -> {
                binding.phoneNumber.error = getString(R.string.error_user_phone)
                false

            }

            address.isEmpty() -> {
                binding.addressText.error = getString(R.string.error_user_address)
                false
            }

            city.isEmpty() -> {
                binding.cityText.error = getString(R.string.error_user_city)
                false
            }

            street.isEmpty() -> {
                binding.streetText.error = getString(R.string.error_user_street)
                false
            }

            pin.isEmpty() -> {
                binding.pinText.error = getString(R.string.error_user_pin)
                false
            }

            selectedImageFile == null -> {
                Toast.makeText(
                    this,
                    getString(R.string.error_please_upload_image),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }
    }

    private fun profileValidation() {
        val userName = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        val address = binding.addressText.text.toString().trim()
        val city = binding.cityText.text.toString().trim()
        val street = binding.streetText.text.toString().trim()
        val pin = binding.pinText.text.toString().trim()

        if (ValidationInputs(userName, email, phoneNumber, address, city, street, pin)) {
            //calling Api here
            updateProfileApi(userId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun updateProfileApi(userId: String) {
        val userName = binding.userName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phoneNumber = binding.phoneNumber.text.toString().trim()
        val address = binding.addressText.text.toString().trim()
        val city = binding.cityText.text.toString().trim()
        val street = binding.streetText.text.toString().trim()
        val pin = binding.pinText.text.toString().trim()

        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userName)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val phoneNoBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)
        val addressBody = RequestBody.create("text/plain".toMediaTypeOrNull(), address)
        val cityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), city)
        val streetBody = RequestBody.create("text/plain".toMediaTypeOrNull(), street)
        val pinBody = RequestBody.create("text/plain".toMediaTypeOrNull(), pin)

        val imagePart = selectedImageFile?.let {
            val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profileImage", it.name, requestBody)
        }

        if (imagePart != null) {
            this?.let {
                updateProfileViewModel.updateProfile(
                    nameBody,
                    emailBody,
                    phoneNoBody,
                    addressBody,
                    cityBody,
                    streetBody,
                    pinBody,
                    imagePart,
                    userId
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
                    binding.profileImg.setImageBitmap(it)
                } ?: Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.image_captured), Toast.LENGTH_SHORT).show()
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
        galleryIntent.type = "image/*" //allow all types image
        selectImageLauncher.launch(galleryIntent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(this.packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        } else {
            Toast.makeText(this, getString(R.string.no_camera_app_found), Toast.LENGTH_SHORT).show()
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
                        binding.profileImg.setImageBitmap(bitmap)
                    } ?: Toast.makeText(
                        this,
                        getString(R.string.failed_load_image),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
}