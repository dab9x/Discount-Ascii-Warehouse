package com.dab.discountascii.networking

import com.dab.discountascii.model.ResponseProducts
import com.dab.discountascii.model.ResponsePurchases
import com.dab.discountascii.model.ResponseUsers
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("/api/users")
    fun getUsers(): Observable<ResponseUsers>

    @GET("/api/purchases/by_user/{username}")
    fun getPurchasesUser(
        @Path("username") username: String
    ): Observable<ResponsePurchases>

    @GET("/api/purchases/by_user/{username}")
    fun getLimitPurchasesUser(
        @Path("username") username: String,
        @Query("limit") limit: Int
    ): Observable<ResponsePurchases>

    @GET("/api/purchases/by_product/{productId}")
    fun getPurchasesProduct(
        @Path("productId") productId: Int
    ): Observable<ResponsePurchases>

    @GET("/api/products/{productId}")
    fun getProductDetail(
        @Path("productId") productId: Int
    ): Observable<ResponseProducts>
}


