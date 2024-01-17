package com.example.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetStoreDetailUsecase
import com.example.presentation.mapper.toStoreDetailModel
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
        Log.d("서버", "서버 통신 시작")
        storeDetailUseCase(nwLong, nwLat, seLong, seLat).collect { storeDetail ->
            val storeDetailList = storeDetail.map { it.toStoreDetailModel() }
            _storeDetailData.value = UiState.Success(storeDetailList)
        }
    }
}