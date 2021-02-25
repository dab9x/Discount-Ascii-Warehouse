package com.dab.discountascii.di

import com.dab.discountascii.data.remote.AppRemoteDataSource
import com.dab.discountascii.data.repository.AppRepository
import com.dab.discountascii.networking.AppService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideAppService(retrofit: Retrofit): AppService =
        retrofit.create(AppService::class.java)

    @Singleton
    @Provides
    fun provideAppRemoteDataSource(appService: AppService) =
        AppRemoteDataSource(appService)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: AppRemoteDataSource) =
        AppRepository(remoteDataSource)
}