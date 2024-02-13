package com.example.domain.usecase

import com.example.domain.model.search.SearchWord
import com.example.domain.repository.SearchWordRepository

class InsertSearchWordUseCase(private val repository: SearchWordRepository) {
    suspend operator fun invoke(searchWord: SearchWord) {
        repository.insertSearchWord(searchWord)
    }
}