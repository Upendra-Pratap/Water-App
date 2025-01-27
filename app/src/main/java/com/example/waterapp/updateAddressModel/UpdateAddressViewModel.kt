package com.example.waterapp.updateAddressModel

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
class UpdateAddressViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mRejectResponse = MutableLiveData<Event<UpdateAddressResponse>>()

    fun updateAddress(
        id: String,
        street: RequestBody,
        city: RequestBody,
        zip: RequestBody,
        id_proof: MultipartBody.Part,

    ) {
        viewModelScope.launch {
            progressIndicator.value = true
            try {
                val response =
                    repository.updateAddress(id, street, city, zip, id_proof)
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