package com.example.waterapp.addBalanceModel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
class AddBalanceViewModel @Inject constructor(application: Application,
    private val repository: CommonRepository): AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mRejectResponse = MutableLiveData<Event<AddBalanceResponse>>()
    var context: Context? = null

    fun addBalance(
        id: String,
        addBalanceBody: AddBalanceBody,
        activity: Activity,
    ) {
        repository.addBalance(
            id,
            addBalanceBody
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AddBalanceResponse>() {
                override fun onNext(value: AddBalanceResponse) {
                    progressIndicator.value = false
                    mRejectResponse.value = Event(value)
                }
                override fun onError(e: Throwable) {
                    progressIndicator.value = false
                    errorResponse.value = e
                }
                override fun onComplete() {
                    progressIndicator.value = false
                }
            })
    }
}