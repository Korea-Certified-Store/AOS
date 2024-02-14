package com.example.domain.usecase

import com.example.domain.repository.SearchWordRepository

class DeleteSearchWordByIdUseCase(private val repository: SearchWordRepository) {
    suspend operator fun invoke(id: Long) {
        repository.deleteSearchWordsById(id)
    }
}