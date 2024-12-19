package com.example.waterapp.updateProfileModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.waterapp.repository.CommonRepository
import com.example.waterapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mRejectResponse = MutableLiveData<Event<UpdateProfileResponse>>()

    fun updateProfile(
        user_name: RequestBody,
        user_email: RequestBody,
        password: RequestBody,
        phone_no: RequestBody,
        street: RequestBody,
        city: RequestBody,
        zip: RequestBody,
        profileImage: MultipartBody.Part,
        id: String
    ) {
        viewModelScope.launch {
            progressIndicator.value = true
            try {
                val response =
                    repository.updateProfile(user_name, user_email, password, phone_no, street, city, zip, profileImage, id)
                if (response.isSuccessful && response.body()?.success == true) {
                    mRejectResponse.value = Event(response.body()!!)
                } else {
                    errorResponse.value =
                        Throwable(response.body()?.message ?: "Registration failed")
                }
            } catch (e: Exception) {
                errorResponse.value = e
            } finally {
                progressIndicator.value = false
            }
        }
    }
}