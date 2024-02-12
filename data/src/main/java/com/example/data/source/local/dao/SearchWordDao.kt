package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.source.local.entity.SearchWord

@Dao
interface SearchWordDao {
    @Query("SELECT * FROM searchword ORDER BY searchTime DESC")
    fun getSearchWords(): List<SearchWord>

    @Query("SELECT COUNT(*) FROM searchword")
    suspend fun getSearchWordCount(): Int

    @Query("DELETE FROM searchword WHERE searchTime = (SELECT MIN(searchTime) FROM searchword)")
    suspend fun deleteOldestSearchWord()

    @Query("SELECT * FROM searchword WHERE keyword = :keyword")
    suspend fun getSearchWordByKeyword(keyword: String): SearchWord?

    @Query("UPDATE searchword SET searchTime = :searchTime WHERE keyword = :keyword")
    suspend fun updateSearchTime(keyword: String, searchTime: Long)

    @Insert
    fun insertSearchWord(searchHistory: SearchWord)

    @Transaction
    suspend fun insertOrUpdateSearch(searchHistory: SearchWord) {
        if (getSearchWordCount() >= 30) {
            deleteOldestSearchWord()
        }
        val existingSearch = getSearchWordByKeyword(searchHistory.keyword)
        if (existingSearch != null) {
            updateSearchTime(searchHistory.keyword, System.currentTimeMillis())
        } else {
            insertSearchWord(searchHistory)
        }
    }
}
