package com.dab.discountascii.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dab.discountascii.base.BaseViewModel
import com.dab.discountascii.model.User
import com.dab.discountascii.networking.ApiModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel() {

    private val users = MutableLiveData<List<User>>()

    fun showUsers(): MutableLiveData<List<User>> {
        return users
    }

    fun getUsers() {
        disposables.add(
            ApiModule.create()
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading(true) }
                .doFinally { showLoading(false) }
                .subscribe({
                    users.value = it.users
                }, {
                    showFailure(it)
                })
        )
    }

    fun filterUser(user: MutableList<User>) {
        users.value = user
    }

    override fun onCleared() {
        disposables.clear()
    }
}