package com.example.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.map.StoreDetail
import com.example.domain.usecase.GetStoreDetailUseCase
import com.example.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getStoreDetailUseCase: GetStoreDetailUseCase) :
    ViewModel() {
    private val _storeDetailModelData =
        MutableStateFlow<UiState<List<StoreDetail>>>(UiState.Loading)
    val storeDetailModelData: StateFlow<UiState<List<StoreDetail>>> =
        _storeDetailModelData.asStateFlow()

    fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ) = viewModelScope.launch {
        getStoreDetailUseCase(
            nwLong,
            nwLat,
            swLong,
            swLat,
            seLong,
            seLat,
            neLong,
            neLat
        ).fold(
            onSuccess = {
                _storeDetailModelData.value = UiState.Success(it)
            }, onFailure = { e ->
                _storeDetailModelData.value = UiState.Failure(e.toString())
            }
        )
    }
}