package com.example.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.StoreDetail
import com.example.domain.usecase.GetStoreDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getStoreDetailUseCase: GetStoreDetailUseCase) :
    ViewModel() {
    private val _storeDetailData = MutableStateFlow<UiState<List<StoreDetail>>>(UiState.Loading)
    val storeDetailData: StateFlow<UiState<List<StoreDetail>>> = _storeDetailData.asStateFlow()

    fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ) = viewModelScope.launch {
        getStoreDetailUseCase(nwLong, nwLat, seLong, seLat).fold(
            onSuccess = {
                _storeDetailData.value = UiState.Success(it)
            }, onFailure = { e ->
                _storeDetailData.value = UiState.Failure(e.toString())
            }
        )
    }
}