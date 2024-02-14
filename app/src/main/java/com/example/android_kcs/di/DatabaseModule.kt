package com.example.android_kcs.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.dao.SearchWordDao
import com.example.data.source.local.database.SearchWordDatabase
import com.example.data.source.local.database.SearchWordDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): SearchWordDatabase = Room
        .databaseBuilder(context, SearchWordDatabase::class.java, DB_NAME)
        .build()

    @Singleton
    @Provides
    fun provideAlbumDao(appDatabase: SearchWordDatabase): SearchWordDao = appDatabase.searchWordDao()
}