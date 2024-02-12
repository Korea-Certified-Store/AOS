package com.example.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.search.SearchWord

@Entity
data class SearchWordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val keyword: String,
    val searchTime: Long
) {
    fun toDomainModel() = SearchWord(
        keyword = keyword,
        searchTime = searchTime
    )
}