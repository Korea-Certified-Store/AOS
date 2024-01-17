package com.example.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetStoreDetailUsecase
import com.example.presentation.model.StoreDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storeDetailUseCase: GetStoreDetailUsecase) :
    ViewModel() {

    private val _storeDetailData =
        MutableStateFlow<UiState<List<StoreDetailModel>>>(UiState.Loading)
    val storeDetailData: StateFlow<UiState<List<StoreDetailModel>>> = _storeDetailData.asStateFlow()

    fun getStoreDetail(
        nwLong: Double,
        nwLat: Double,
        seLong: Double,
        seLat: Double
    ) = viewModelScope.launch {
        storeDetailUseCase.invoke(
            nwLong,
            nwLat,
            seLong,
            seLat
        ).onSuccess {
            Log.d("MainViewModel 서버 통신", "$it")
        }.onFailure {
            Log.d("MainUser 서버 통신", "$it")
        }
    }
}