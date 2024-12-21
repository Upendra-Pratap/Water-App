package com.example.waterapp.notificationModel.allNotificationDelete

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.waterapp.R
import com.example.waterapp.classes.CustomProgressDialog
import com.example.waterapp.notificationModel.DeleteNotificationModel.DeleteNotificationResponse
import com.example.waterapp.repository.CommonRepository
import com.example.waterapp.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AllNotificationDeleteVewModel @Inject constructor(application: Application, private val repository: CommonRepository
): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mDeleteNotificationResponse = MutableLiveData<Event<AllNotificationDeleteResponse>>()
    var context: Context? = null

    fun allNotificationDelete(
        id: String,
        progressDialog: CustomProgressDialog,
        activity: Activity,

        ) =
        viewModelScope.launch {
            getDeleteList(id,progressDialog,activity)
        }

    private suspend fun getDeleteList(
        id: String,
        progressDialog: CustomProgressDialog,
        activity: Activity,
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.allNotificationDelete(
            id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AllNotificationDeleteResponse>() {
                override fun onNext(value: AllNotificationDeleteResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mDeleteNotificationResponse.value = Event(value)
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