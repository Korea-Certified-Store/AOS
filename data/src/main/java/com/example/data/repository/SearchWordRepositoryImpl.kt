package com.example.data.repository

import com.example.data.source.SearchWordDataSource
import com.example.domain.model.search.SearchWord
import com.example.domain.repository.SearchWordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchWordRepositoryImpl @Inject constructor(private val dataSource: SearchWordDataSource) :
    SearchWordRepository {
    override fun getRecentSearchWords(): Flow<List<SearchWord>> {
        return dataSource.getRecentSearchWords()
    }

    override suspend fun insertSearchWord(searchWord: SearchWord) {
        dataSource.insertSearchWord(searchWord)
    }

    override suspend fun deleteAllSearchWords() {
        dataSource.deleteAllSearchWords()
    }

    override suspend fun deleteSearchWordsById(id: Long) {
        dataSource.deleteSearchWordsById(id)
    }
}