package com.example.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.source.local.dao.SearchWordDao
import com.example.data.source.local.entity.SearchWord

@Database(entities = [SearchWord::class], version = 1)
abstract class SearchWordDatabase: RoomDatabase() {
    abstract fun searchWordDao(): SearchWordDao

    companion object {
        const val DB_NAME = "KCS.db"
    }
}