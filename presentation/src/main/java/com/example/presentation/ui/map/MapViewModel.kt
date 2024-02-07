package com.example.presentation.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.map.StoreDetail
import com.example.domain.usecase.GetStoreDetailUseCase
import com.example.domain.util.Resource
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.util.MainConstants.FAIL_TO_LOAD_DATA
import com.example.presentation.util.MainConstants.GREAT_STORE
import com.example.presentation.util.MainConstants.KIND_STORE
import com.example.presentation.util.MainConstants.SAFE_STORE
import com.example.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val getStoreDetailUseCase: GetStoreDetailUseCase) :
    ViewModel() {

    private val _ableToShowSplashScreen = MutableStateFlow(true)
    val ableToShowSplashScreen: StateFlow<Boolean> = _ableToShowSplashScreen

    var ableToShowInitialMarker = true

    private val filterSet = mutableSetOf<String>()

    private val _storeDetailModelData =
        MutableStateFlow<UiState<List<List<StoreDetail>>>>(UiState.Loading)
    val storeDetailModelData: StateFlow<UiState<List<List<StoreDetail>>>> =
        _storeDetailModelData.asStateFlow()

    private val _isLocationPermissionGranted = MutableStateFlow(false)
    val isLocationPermissionGranted: StateFlow<Boolean> get() = _isLocationPermissionGranted

    val flattenedStoreDetailList: MutableStateFlow<List<StoreDetail>> =
        MutableStateFlow(emptyList())

    private val _isInitialize = MutableStateFlow(true)
    val isInitialize get() = _isInitialize

    fun showMoreStore(count: Int) {
        val newItem: List<StoreDetail> = when (val uiState = _storeDetailModelData.value) {
            is UiState.Success -> uiState.data.getOrNull(count) ?: emptyList()
            else -> emptyList()
        }
        val currentList = flattenedStoreDetailList.value.toMutableList()
        newItem.forEach { currentList.add(it) }
        flattenedStoreDetailList.value = currentList.toList()
    }

    fun updateSplashState() {
        _ableToShowSplashScreen.value = false
    }

    fun getFilterSet(): Set<String> {
        return if (filterSet.isEmpty()) setOf(SAFE_STORE, GREAT_STORE, KIND_STORE)
        else filterSet.toSet()
    }

    fun updateFilterSet(certificationName: String, isClicked: Boolean) {
        if (isClicked) {
            filterSet.add(certificationName)
        } else {
            filterSet.remove(certificationName)
        }
    }

    fun setLocationTrackingMode(): LocationTrackingButton {
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
        ).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _storeDetailModelData.value = UiState.Success(result.data ?: emptyList())
                }

                is Resource.Failure -> {
                    _storeDetailModelData.value =
                        UiState.Failure(result.message ?: FAIL_TO_LOAD_DATA)
                }

                is Resource.Loading -> {
                    _storeDetailModelData.value = UiState.Loading
                }
            }
            flattenedStoreDetailList.value = emptyList()
            if (_isInitialize.value) {
                flattenedStoreDetailList.value = result.data?.flatten() ?: emptyList()
            } else {
                showMoreStore(0)
            }
        }
    }

    fun updateIsInitialize() {
        _isInitialize.value = false
    }
}