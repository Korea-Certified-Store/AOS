package com.example.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.map.StoreDetail
import com.example.domain.usecase.SearchStoreUseCase
import com.example.domain.util.Resource
import com.example.presentation.util.MainConstants
import com.example.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchStoreUseCase: SearchStoreUseCase) :
    ViewModel() {

    private val _searchStoreModelData =
        MutableStateFlow<UiState<List<StoreDetail>>>(UiState.Loading)
    val searchStoreModelData: StateFlow<UiState<List<StoreDetail>>> =
        _searchStoreModelData.asStateFlow()

    fun searchStore(
        currLong: Double,
        currLat: Double,
        searchKeyword: String
    ) = viewModelScope.launch {
        searchStoreUseCase(
            currLong, currLat, searchKeyword
        ).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _searchStoreModelData.value = UiState.Success(result.data ?: emptyList())
                }

                is Resource.Failure -> {
                    _searchStoreModelData.value =
                        UiState.Failure(result.message ?: MainConstants.FAIL_TO_LOAD_DATA)
                }

                is Resource.Loading -> {
                    _searchStoreModelData.value = UiState.Loading
                }
            }
        }
    }
}