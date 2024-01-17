package com.example.presentation.ui

import android.util.Log
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
    private val _uiState = MutableStateFlow<UiState<List<StoreDetail>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<StoreDetail>>> = _uiState

    private val _storeDetailData =
        MutableStateFlow<List<StoreDetail>>(emptyList())
    val storeDetailData: StateFlow<List<StoreDetail>> = _storeDetailData.asStateFlow()

    fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ) {
        if (uiState.value == _storeDetailData.value) return
        viewModelScope.launch {
            getStoreDetailUseCase(nwLong, nwLat, seLong, seLat).onSuccess {
                _uiState.emit(UiState.Success(_storeDetailData.value))
            }.onFailure {
                _uiState.emit(UiState.Failure("실패"))
                Log.d("ViewModel UiState 통신 결과", _uiState.value.getUiStateModel().toString())
            }
        }
    }
}