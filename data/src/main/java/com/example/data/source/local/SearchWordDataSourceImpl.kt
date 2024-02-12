package com.example.data.source.local

import com.example.data.source.SearchWordDataSource
import com.example.data.source.local.dao.SearchWordDao
import com.example.data.source.local.entity.SearchWordEntity
import com.example.domain.model.search.SearchWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchWordDataSourceImpl @Inject constructor(private val searchWordDao: SearchWordDao) :
    SearchWordDataSource {
    override fun getRecentSearchWords(): Flow<List<SearchWord>> {
        return searchWordDao.getSearchWords().map { items -> items.map { word -> word.toDomainModel() } }
    }

    override suspend fun insertSearchWord(searchWord: SearchWord) {
        searchWordDao.insertOrUpdateSearch(SearchWordEntity(keyword = searchWord.keyword, searchTime = searchWord.searchTime))
    }
}