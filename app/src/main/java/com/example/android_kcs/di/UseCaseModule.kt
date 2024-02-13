package com.example.android_kcs.di

import com.example.domain.repository.SearchStoreRepository
import com.example.domain.repository.SearchWordRepository
import com.example.domain.repository.StoreDetailRepository
import com.example.domain.usecase.DeleteAllSearchWordsUseCase
import com.example.domain.usecase.DeleteSearchWordByIdUseCase
import com.example.domain.usecase.GetRecentSearchWordUseCase
import com.example.domain.usecase.GetStoreDetailUseCase
import com.example.domain.usecase.InsertSearchWordUseCase
import com.example.domain.usecase.SearchStoreUseCase
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

    @Singleton
    @Provides
    fun provideSearchStoreUseCase(repository: SearchStoreRepository): SearchStoreUseCase {
        return SearchStoreUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetRecentSearchWordUseCase(repository: SearchWordRepository): GetRecentSearchWordUseCase {
        return GetRecentSearchWordUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideInsertSearchWordUseCase(repository: SearchWordRepository): InsertSearchWordUseCase {
        return InsertSearchWordUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteAllSearchWordsUseCase(repository: SearchWordRepository): DeleteAllSearchWordsUseCase {
        return DeleteAllSearchWordsUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteSearchWordByIdUseCase(repository: SearchWordRepository): DeleteSearchWordByIdUseCase {
        return DeleteSearchWordByIdUseCase(repository)
    }
}