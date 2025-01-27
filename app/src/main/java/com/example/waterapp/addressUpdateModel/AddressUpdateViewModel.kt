package com.example.waterapp.addressUpdateModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.waterapp.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class AddressUpdateViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
}