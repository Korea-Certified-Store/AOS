package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.source.local.entity.SearchWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchWordDao {
    @Query("SELECT * FROM SearchWordEntity ORDER BY searchTime DESC")
    fun getSearchWords(): Flow<List<SearchWordEntity>>

    @Query("SELECT COUNT(*) FROM SearchWordEntity")
    suspend fun getSearchWordCount(): Int

    @Query("DELETE FROM SearchWordEntity WHERE searchTime = (SELECT MIN(searchTime) FROM SearchWordEntity)")
    suspend fun deleteOldestSearchWord()

    @Query("DELETE FROM SearchWordEntity")
    suspend fun deleteAllSearchWords()

    @Query("SELECT * FROM SearchWordEntity WHERE keyword = :keyword")
    suspend fun getSearchWordByKeyword(keyword: String): SearchWordEntity?

    @Query("UPDATE SearchWordEntity SET searchTime = :searchTime WHERE keyword = :keyword")
    suspend fun updateSearchTime(keyword: String, searchTime: Long)

    @Insert
    suspend fun insertSearchWord(searchWord: SearchWordEntity)

    @Transaction
    suspend fun insertOrUpdateSearch(searchWord: SearchWordEntity) {
        if (getSearchWordCount() >= 30) {
            deleteOldestSearchWord()
        }
        val existingSearch = getSearchWordByKeyword(searchWord.keyword)
        if (existingSearch != null) {
            updateSearchTime(searchWord.keyword, System.currentTimeMillis())
        } else {
            insertSearchWord(searchWord)
        }
    }
}
