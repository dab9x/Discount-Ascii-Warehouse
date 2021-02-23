package com.dab.discountascii.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    val disposables = CompositeDisposable()
    val eventLoading = MutableLiveData<Event<Boolean>>()
    val eventFailure = MutableLiveData<Event<Throwable>>()

    fun showLoading(value: Boolean) {
        eventLoading.value = Event(value)
    }

    fun showFailure(throwable: Throwable) {
        eventFailure.value = Event(throwable)
    }
}
