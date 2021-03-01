package com.dab.discountascii.viewmodel

import androidx.lifecycle.*
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.data.entities.Purchase
import com.dab.discountascii.data.entities.ResponsePurchases
import com.dab.discountascii.data.repository.AppRepository
import com.dab.discountascii.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class UsersPurchaseViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var job: Job = Job()

    private var _username = MutableLiveData<String>()
    private val products = MutableLiveData<Resource<List<Product>>>()
    private val productsPopular = MutableLiveData<Resource<List<Product>>>()

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    fun getProducts(): LiveData<Resource<List<Product>>> {
        return products
    }

    fun getProductsPopular(): LiveData<Resource<List<Product>>> {
        return productsPopular
    }

    fun fetchProducts() {
        viewModelScope.launch {
            products.postValue(Resource.loading())
            try {
                coroutineScope {

                    val purchaseUsersFromApi: Resource<ResponsePurchases>?

                    val purchaseUsersFromApiDeferred =
                        viewModelScope.async { repository.getPurchasesUser(_username.value!!) }

                    try {
                        purchaseUsersFromApi = purchaseUsersFromApiDeferred.await()
                    } catch (ex: Exception) {
                        val message = ex.toString()
                        products.postValue(Resource.error(message))
                        return@coroutineScope
                    }

                    val allPurchasesFromApi = mutableListOf<Purchase>()

                    if (purchaseUsersFromApi.status == Resource.Status.SUCCESS) {
                        purchaseUsersFromApi.data?.purchases?.let {
                            allPurchasesFromApi.addAll(it)
                        }

                        coroutineScope {
                            val productsDetail: List<Product>?
                            val productsUserRecent: Map<String, List<String>>?

                            val productsDetailDeferred = viewModelScope.async {
                                fetchProductsDetail(allPurchasesFromApi.asIterable())
                            }
                            val productsUserRecentDeferred = viewModelScope.async {
                                fetchProductsWithUserPurchaseRecent(allPurchasesFromApi.asIterable())
                            }

                            try {
                                productsDetail = productsDetailDeferred.await()
                                productsUserRecent = productsUserRecentDeferred.await()
                            } catch (ex: Exception) {
                                val message = ex.toString()
                                products.postValue(Resource.error(message))
                                return@coroutineScope
                            }

                            processData(false, productsDetail, productsUserRecent)
                        }
                    } else if (purchaseUsersFromApi.status == Resource.Status.ERROR) {
                        var message = "Oops! An error occurred. Please try again later"
                        purchaseUsersFromApi.message?.let {
                            message = it
                        }
                        products.postValue(Resource.error(message))
                    }
                }
            } catch (e: Exception) {
                products.postValue(Resource.error(e.toString()))
            }
        }
    }

    fun fetchProductsPopular () {
        viewModelScope.launch {
            productsPopular.postValue(Resource.loading())
            try {
                coroutineScope {
                    val purchaseUsersFromApiDeferred =
                        viewModelScope.async {
                            repository.getLimitPurchasesUser(
                                _username.value!!,
                                5
                            )
                        }
                    val purchaseUsersFromApi = purchaseUsersFromApiDeferred.await()

                    val allPurchasesFromApi = mutableListOf<Purchase>()

                    if (purchaseUsersFromApi.status == Resource.Status.SUCCESS) {
                        purchaseUsersFromApi.data?.purchases?.let {
                            allPurchasesFromApi.addAll(it)
                        }
                        coroutineScope {
                            val productsDetailDeferred = viewModelScope.async {
                                fetchProductsDetail(allPurchasesFromApi.asIterable())
                            }
                            val productsUserRecentDeferred = viewModelScope.async {
                                fetchProductsWithUserPurchaseRecent(allPurchasesFromApi.asIterable())
                            }

                            val productsDetail = productsDetailDeferred.await()
                            val productsUserRecent = productsUserRecentDeferred.await()

                            processData(true, productsDetail, productsUserRecent)
                        }
                    } else if (purchaseUsersFromApi.status == Resource.Status.ERROR) {
                        var message = "Oops! An error occurred. Please try again later"
                        purchaseUsersFromApi.message?.let {
                            message = it
                        }
                        productsPopular.postValue(Resource.error(message))
                    }
                }
            } catch (e: Exception) {
                productsPopular.postValue(Resource.error(e.toString()))
            }
        }
    }

    private fun processData(isPopular: Boolean, productDetails: List<Product>, userRecent: Map<String, List<String>>) {
        val productsResult = ArrayList<Product>()

        for (product in productDetails) {
            product.recent = userRecent[(product.id).toString()]!!
            productsResult.add(product)
        }

        if (isPopular)
            productsPopular.postValue(Resource.success(productsResult))
        else
            products.postValue(Resource.success(productsResult))
    }

    suspend fun fetchProductsDetail(purchases: Iterable<Purchase>): List<Product> =
        withContext(Dispatchers.IO) {
            val products = mutableListOf<Product>()
            purchases.forEach { purchase ->
                coroutineScope {
                    val productDetailFromApiDeferred =
                        viewModelScope.async { repository.getProductDetail(purchase.productId) }
                    val productDetailFromApi = productDetailFromApiDeferred.await()

                    if (productDetailFromApi.status == Resource.Status.SUCCESS) {
                        productDetailFromApi.data?.product?.let {
                            products.add(it)
                        }
                    }
                }
            }
            return@withContext products
        }

    private suspend fun fetchProductsWithUserPurchaseRecent(purchases: Iterable<Purchase>): Map<String, List<String>> =
        withContext(Dispatchers.IO) {
            val productItems = hashMapOf<String, List<String>>()
            purchases.forEach { purchase ->
                coroutineScope {
                    val purchaseProductFromApiDeferred =
                        viewModelScope.async { repository.getPurchasesProduct(purchase.productId) }
                    val purchaseProductFromApi = purchaseProductFromApiDeferred.await()

                    if (purchaseProductFromApi.status == Resource.Status.SUCCESS) {
                        purchaseProductFromApi.data?.purchases?.let {
                            val listRecent = ArrayList<String>()
                            for (item in it) {
                                if (!listRecent.contains(item.username)) {
                                    listRecent.add(item.username)
                                }
                            }
                            productItems.put((purchase.productId).toString(), listRecent)
                        }
                    }
                }
            }
            return@withContext productItems
        }

    fun start(username: String) {
        _username.value = username
    }
}