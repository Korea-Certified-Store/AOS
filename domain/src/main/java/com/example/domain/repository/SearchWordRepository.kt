package com.example.domain.repository

import com.example.domain.model.search.SearchWord
import kotlinx.coroutines.flow.Flow

interface SearchWordRepository {
    fun getRecentSearchWords(): Flow<List<SearchWord>>

    suspend fun insertSearchWord(searchWord: SearchWord)
}