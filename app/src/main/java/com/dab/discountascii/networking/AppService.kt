package com.dab.discountascii.networking

import com.dab.discountascii.data.entities.ResponseProducts
import com.dab.discountascii.data.entities.ResponsePurchases
import com.dab.discountascii.data.entities.ResponseUsers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @GET("/api/users")
    suspend fun getUsers(): Response<ResponseUsers>

    @GET("/api/purchases/by_user/{username}")
    suspend fun getPurchasesUser(
        @Path("username") username: String
    ): Response<ResponsePurchases>

    @GET("/api/purchases/by_user/{username}")
    suspend fun getLimitPurchasesUser(
        @Path("username") username: String,
        @Query("limit") limit: Int
    ): Response<ResponsePurchases>

    @GET("/api/purchases/by_product/{productId}")
    suspend fun getPurchasesProduct(
        @Path("productId") productId: Int
    ): Response<ResponsePurchases>

    @GET("/api/products/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: Int
    ): Response<ResponseProducts>
}