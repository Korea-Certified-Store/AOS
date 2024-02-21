package com.example.android_kcs.di

import com.example.data.source.SearchStoreDataSource
import com.example.data.source.SearchWordDataSource
import com.example.data.source.StoreDetailDataSource
import com.example.data.source.local.SearchWordDataSourceImpl
import com.example.data.source.remote.SearchStoreSourceImpl
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
    abstract fun providesStoreDetailSource(dataSourceImpl: StoreDetailSourceImpl): StoreDetailDataSource

    @Singleton
    @Binds
    abstract fun providesSearchStoreSource(dataSourceImpl: SearchStoreSourceImpl): SearchStoreDataSource

    @Singleton
    @Binds
    abstract fun providesSearchWordSource(dataSourceImpl: SearchWordDataSourceImpl): SearchWordDataSource

}