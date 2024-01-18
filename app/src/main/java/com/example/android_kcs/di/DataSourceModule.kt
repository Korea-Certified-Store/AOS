package com.example.android_kcs.di

import com.example.data.source.StoreDetailDataSource
import com.example.data.source.remote.StoreDetailSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun providesStoreDetailSource(DataSourceImpl: StoreDetailSourceImpl): StoreDetailDataSource

}