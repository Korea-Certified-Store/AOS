package com.example.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.StoreDetailModel
import com.example.domain.usecase.GetStoreDetailUseCase
import com.example.presentation.model.LocationTrackingButton
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
        MutableStateFlow<UiState<List<StoreDetailModel>>>(UiState.Loading)
    val storeDetailModelData: StateFlow<UiState<List<StoreDetailModel>>> =
        _storeDetailModelData.asStateFlow()

    private val _isLocationPermissionGranted = MutableStateFlow<Boolean>(false)
    val isLocationPermissionGranted: StateFlow<Boolean> get() = _isLocationPermissionGranted

    fun getInitialLocationTrackingMode(): LocationTrackingButton {
        return if (isLocationPermissionGranted.value) {
            LocationTrackingButton.FOLLOW
        } else {
            LocationTrackingButton.NONE
        }
    }

    fun updateLocationPermission(isGranted: Boolean) {
        _isLocationPermissionGranted.value = isGranted
    }

    fun checkAndUpdatePermission(context: Context) {
        _isLocationPermissionGranted.value = !(ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)
    }

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
            neLat,
        ).fold(
            onSuccess = {
                _storeDetailModelData.value = UiState.Success(it)
            }, onFailure = { e ->
                _storeDetailModelData.value = UiState.Failure(e.toString())
            }
        )
    }
}