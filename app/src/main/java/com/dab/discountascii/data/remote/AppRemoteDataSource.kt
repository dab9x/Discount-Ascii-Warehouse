package com.dab.discountascii.data.remote

import com.dab.discountascii.networking.AppService
import javax.inject.Inject

class AppRemoteDataSource @Inject constructor(
    private val appService: AppService
) : BaseDataSource() {

    suspend fun getUsers() = getResult { appService.getUsers() }

    suspend fun getPurchasesUser(username: String) =
        getResult { appService.getPurchasesUser(username) }

    suspend fun getLimitPurchasesUser(username: String, limit: Int) =
        getResult { appService.getLimitPurchasesUser(username, limit) }

    suspend fun getPurchasesProduct(productId: Int) =
        getResult { appService.getPurchasesProduct(productId) }

    suspend fun getProductDetail(productId: Int) =
        getResult { appService.getProductDetail(productId) }

}