package com.dab.discountascii.viewmodel

import androidx.lifecycle.*
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.data.entities.Purchase
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

    init {
        _username = MutableLiveData<String>()
        fetchProducts()
        fetchProductsPopular()
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
                    val purchaseUsersFromApiDeferred =
                        viewModelScope.async { repository.getPurchasesUser(_username.value!!) }
                    val purchaseUsersFromApi = purchaseUsersFromApiDeferred.await()

                    val allPurchasesFromApi = mutableListOf<Purchase>()

                    if (purchaseUsersFromApi.status == Resource.Status.SUCCESS) {
                        purchaseUsersFromApi.data?.purchases?.let {
                            allPurchasesFromApi.addAll(it)
                        }

                        val productsDetail =
                            fetchProductsDetail(allPurchasesFromApi.asIterable())
                        val productsUser = fetchProductsWithUser(productsDetail.asIterable())

                        products.postValue(Resource.success(productsUser))
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

    fun fetchProductsPopular() {
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

                        val productsDetail =
                            fetchProductsDetail(allPurchasesFromApi.asIterable())
                        val productsUser = fetchProductsWithUser(productsDetail.asIterable())

                        productsPopular.postValue(Resource.success(productsUser))
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

    private suspend fun fetchProductsDetail(purchases: Iterable<Purchase>): List<Product> =
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

    private suspend fun fetchProductsWithUser(products: Iterable<Product>): List<Product> =
        withContext(Dispatchers.IO) {
            val productItems = mutableListOf<Product>()
            products.forEach { product ->
                coroutineScope {
                    val purchaseProductFromApiDeferred =
                        viewModelScope.async { repository.getPurchasesProduct(product.id) }
                    val purchaseProductFromApi = purchaseProductFromApiDeferred.await()

                    if (purchaseProductFromApi.status == Resource.Status.SUCCESS) {
                        purchaseProductFromApi.data?.purchases?.let {
                            val productUpdated: Product = product
                            val listRecent = ArrayList<String>()
                            for (purchase in it) {
                                if (!listRecent.contains(purchase.username)) {
                                    listRecent.add(purchase.username)
                                }
                            }
                            productUpdated.recent = listRecent
                            productItems.add(productUpdated)
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