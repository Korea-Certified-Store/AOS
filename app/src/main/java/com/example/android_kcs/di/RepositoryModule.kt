package com.example.android_kcs.di

import com.example.data.repository.SearchStoreRepositoryImpl
import com.example.data.repository.StoreDetailRepositoryImpl
import com.example.domain.repository.SearchStoreRepository
import com.example.domain.repository.StoreDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    abstract fun providesStoreDetailRepository(repositoryImpl: StoreDetailRepositoryImpl): StoreDetailRepository

    @Binds
    @Singleton
    abstract fun providesSearchStoreRepository(repositoryImpl: SearchStoreRepositoryImpl): SearchStoreRepository
}