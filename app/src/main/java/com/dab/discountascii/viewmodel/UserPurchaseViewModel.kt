package com.dab.discountascii.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dab.discountascii.base.BaseViewModel
import com.dab.discountascii.model.Product
import com.dab.discountascii.model.ResponseProducts
import com.dab.discountascii.networking.ApiModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class UserPurchaseViewModel : BaseViewModel() {

    private val productPopular = MutableLiveData<List<Product>>()
    private val products = MutableLiveData<MutableList<Product>>()

    fun showProductPopular(): MutableLiveData<List<Product>> {
        return productPopular
    }

    fun showProducts(): MutableLiveData<MutableList<Product>> {
        return products
    }

    fun clearProducts() {
        products.value = mutableListOf()
        productPopular.value = mutableListOf()
    }

    fun getProducts(username: String) {
        disposables.add(
            ApiModule.create().getPurchasesUser(username)
                .switchMap {
                    Observable.fromIterable(it.purchases.asIterable())
                        .flatMap { it1 ->
                            getProductDetail(it1.productId)
                        }
                }
                .switchMap {
                    Observable.just(it.product)
                        .flatMap { it1 ->
                            getPurchaseByProduct(it1)
                        }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading(true) }
                .doFinally { showLoading(false) }
                .subscribe({
                    val list = ArrayList<Product>()
                    products.value?.let { it1 -> list.addAll(it1) }
                    list.add(it)
                    products.value = list
                }, {
                    showFailure(it)
                })
        )
    }

    private fun getProductDetail(productId: Int): Observable<ResponseProducts> {
        return ApiModule.create().getProductDetail(productId)
    }

    fun getPopularProduct(username: String) {
        disposables.add(
            ApiModule.create().getLimitPurchasesUser(username, 5)
                .switchMap {
                    Observable.fromIterable(it.purchases.asIterable())
                        .flatMap { it1 ->
                            getProductDetail(it1.productId)
                        }
                }
                .switchMap {
                    Observable.just(it.product)
                        .flatMap { it1 ->
                            getPurchaseByProduct(it1)
                        }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showLoading(true) }
                .doFinally { showLoading(false) }
                .subscribe({
                    val list = ArrayList<Product>()
                    productPopular.value?.let { it1 -> list.addAll(it1) }
                    list.add(it)
                    val listSorted =
                        list.sortedWith(compareByDescending { it1 ->
                            it1.price
                            it1.recent.size
                        })
                    productPopular.value = listSorted
                }, {
                    showFailure(it)
                })
        )
    }

    private fun getPurchaseByProduct(product: Product): Observable<Product> {
        return ApiModule.create().getPurchasesProduct(product.id)
            .map {
                val productUpdated: Product = product
                val listRecent = ArrayList<String>()
                for (purchase in it.purchases) {
                    if (!listRecent.contains(purchase.username)) {
                        listRecent.add(purchase.username)
                    }
                }
                productUpdated.recent = listRecent
                productUpdated
            }
    }

    override fun onCleared() {
        disposables.clear()
    }
}