package com.example.android_kcs.di

import com.example.domain.repository.StoreDetailRepository
import com.example.domain.usecase.GetStoreDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideStoreDetailUseCase(repository: StoreDetailRepository): GetStoreDetailUseCase {
        return GetStoreDetailUseCase(repository)
    }
}