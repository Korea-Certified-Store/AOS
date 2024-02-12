package com.example.data.source

import com.example.domain.model.search.SearchWord
import kotlinx.coroutines.flow.Flow

interface SearchWordDataSource {
    fun getRecentSearchWords(): Flow<List<SearchWord>>

    suspend fun insertSearchWord(searchWord: SearchWord)
}