package com.example.domain.usecase

import com.example.domain.model.search.SearchWord
import com.example.domain.repository.SearchWordRepository
import kotlinx.coroutines.flow.Flow

class GetRecentSearchWordUseCase(private val repository: SearchWordRepository) {
    suspend operator fun invoke(searchWord: SearchWord): Flow<List<SearchWord>> {
        return repository.getRecentSearchWords()
    }
}