package com.dab.discountascii.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dab.discountascii.R
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.data.entities.Purchase
import com.dab.discountascii.data.entities.User
import com.dab.discountascii.data.repository.AppRepository
import com.dab.discountascii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var job: Job = Job()

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    private var _users = MutableLiveData<Resource<List<User>>>()

    init {
        _users = MutableLiveData<Resource<List<User>>>()
        fetchUsers()
    }


    fun getUsers(): LiveData<Resource<List<User>>> {
        return _users
    }

    fun filterUser(user: MutableList<User>) {
        _users.postValue(Resource.success(user))
    }

    fun fetchUsers() {
        viewModelScope.launch {
            _users.postValue(Resource.loading())
            try {
                coroutineScope {
                    val usersFromApiDeferred =
                        viewModelScope.async { repository.getUsers() }
                    val usersFromApi = usersFromApiDeferred.await()

                    val allUserFromApi = mutableListOf<User>()

                    if (usersFromApi.status == Resource.Status.SUCCESS) {
                        usersFromApi.data?.users?.let {
                            allUserFromApi.addAll(it)
                        }
                        _users.postValue(Resource.success(allUserFromApi))
                    } else if (usersFromApi.status == Resource.Status.ERROR) {
                        var message = "Oops! An error occurred. Please try again later"
                        usersFromApi.message?.let {
                            message = it
                        }
                        _users.postValue(Resource.error(message))
                    }
                }
            } catch (e: Exception) {
                _users.postValue(Resource.error(e.toString()))
            }
        }
    }
}