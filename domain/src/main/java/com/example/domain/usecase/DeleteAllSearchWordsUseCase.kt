package com.example.domain.usecase

import com.example.domain.repository.SearchWordRepository

class DeleteAllSearchWordsUseCase(private val repository: SearchWordRepository) {
    suspend operator fun invoke() {
        repository.deleteAllSearchWords()
    }
}