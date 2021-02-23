package com.dab.discountascii.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiModule {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8000"
        private val apiInterface: ApiService? = null
        private val logging: HttpLoggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
        private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

        fun create(): ApiService {
            if (apiInterface != null) {
                return apiInterface
            }
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}