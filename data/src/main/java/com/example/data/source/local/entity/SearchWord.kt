package com.example.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchWord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val keyword: String,
    val searchTime: Long
)