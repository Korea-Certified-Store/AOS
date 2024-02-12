package com.example.data.source.local.database

import androidx.room.Database
import com.example.data.source.local.dao.SearchWordDao
import com.example.data.source.local.entity.SearchWord

@Database(entities = [SearchWord::class], version = 1)
abstract class SearchWordDatabase {
    abstract fun searchWordDao(): SearchWordDao
}