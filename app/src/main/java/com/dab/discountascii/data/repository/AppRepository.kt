package com.dab.discountascii.data.repository

import com.dab.discountascii.data.entities.ResponseProducts
import com.dab.discountascii.data.entities.ResponsePurchases
import com.dab.discountascii.data.entities.ResponseUsers
import com.dab.discountascii.data.remote.AppRemoteDataSource
import com.dab.discountascii.utils.Resource
import com.dab.discountascii.utils.performGetOperation
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: AppRemoteDataSource
) {

    fun getLiveDataUsers() = performGetOperation(
        networkCall = { remoteDataSource.getUsers() }
    )

    suspend fun getUsers(): Resource<ResponseUsers> {
        return remoteDataSource.getUsers()
    }

    suspend fun getPurchasesUser(username: String): Resource<ResponsePurchases> {
        return remoteDataSource.getPurchasesUser(username)
    }

    suspend fun getLimitPurchasesUser(username: String, limit: Int): Resource<ResponsePurchases> {
        return remoteDataSource.getLimitPurchasesUser(username, limit)
    }

    suspend fun getProductDetail(productId: Int): Resource<ResponseProducts> {
        return remoteDataSource.getProductDetail(productId)
    }

    suspend fun getPurchasesProduct(productId: Int): Resource<ResponsePurchases> {
        return remoteDataSource.getPurchasesProduct(productId)
    }

}