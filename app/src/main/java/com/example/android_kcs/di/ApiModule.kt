package com.example.android_kcs.di

import com.example.data.source.remote.api.SearchStoreApiService
import com.example.data.source.remote.api.StoreDetailApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideStoreDetailService(retrofit: Retrofit): StoreDetailApiService =
        retrofit.create(StoreDetailApiService::class.java)

    @Provides
    @Singleton
    fun provideSearchStoreService(retrofit: Retrofit): SearchStoreApiService =
        retrofit.create(SearchStoreApiService::class.java)
}