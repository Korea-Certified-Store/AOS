package com.example.domain.usecase

import com.example.domain.repository.SearchWordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentSearchWordUseCase(private val repository: SearchWordRepository) {
    suspend operator fun invoke(): Flow<List<String>> {
        return repository.getRecentSearchWords().map { items -> items.map { it.keyword } }
    }
}