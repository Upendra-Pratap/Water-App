package com.example.waterapp.addressUpdateModel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.repository.CommonRepository
import com.example.waterapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class MyAllRequestForAddressUpdateViewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mRejectResponse = MutableLiveData<Event<MyAllRequestForAddressUpdateResponse>>()
    var context: Context? = null

    fun myUpdateAddress(
        id: String,
        activity: Activity,
        progressDialog: CustomProgressDialog
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))

        progressIndicator.value = true
        repository.myAllRequestAddressUpdate(
            id
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<MyAllRequestForAddressUpdateResponse>() {
                override fun onNext(value: MyAllRequestForAddressUpdateResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mRejectResponse.value = Event(value)
                }
                override fun onError(e: Throwable) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    errorResponse.value = e
                }
                override fun onComplete() {
                    progressDialog.stop()
                    progressIndicator.value = false
                }
            })
    }
}