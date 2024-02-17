package com.example.presentation.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.map.StoreDetail
import com.example.domain.usecase.GetStoreDetailUseCase
import com.example.domain.usecase.SearchStoreUseCase
import com.example.domain.util.Resource
import com.example.presentation.model.Coordinate
import com.example.presentation.model.LocationTrackingButton
import com.example.presentation.util.MainConstants.FAIL_TO_LOAD_DATA
import com.example.presentation.util.MainConstants.GREAT_STORE
import com.example.presentation.util.MainConstants.INITIALIZE_ABLE
import com.example.presentation.util.MainConstants.KIND_STORE
import com.example.presentation.util.MainConstants.SAFE_STORE
import com.example.presentation.util.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getStoreDetailUseCase: GetStoreDetailUseCase,
    private val searchStoreUseCase: SearchStoreUseCase,
) :
    ViewModel() {

    private val _ableToShowSplashScreen = MutableStateFlow(true)
    val ableToShowSplashScreen: StateFlow<Boolean> = _ableToShowSplashScreen

    private val filterSet = mutableSetOf<String>()

    private val _storeDetailModelData =
        MutableStateFlow<UiState<List<List<StoreDetail>>>>(UiState.Loading)
    val storeDetailModelData: StateFlow<UiState<List<List<StoreDetail>>>> =
        _storeDetailModelData.asStateFlow()

    private val _isLocationPermissionGranted = MutableStateFlow(false)
    val isLocationPermissionGranted: StateFlow<Boolean> get() = _isLocationPermissionGranted

    private val _flattenedStoreDetailList: MutableStateFlow<List<StoreDetail>> =
        MutableStateFlow(emptyList())
    val flattenedStoreDetailList: StateFlow<List<StoreDetail>> = _flattenedStoreDetailList

    private val _storeInitializeState = MutableStateFlow(INITIALIZE_ABLE)
    val storeInitializeState: StateFlow<Int> get() = _storeInitializeState

    private val _isInitialize = MutableStateFlow(true)
    val isInitialize get() = _isInitialize

    private val _searchStoreModelData =
        MutableStateFlow<UiState<List<StoreDetail>>>(UiState.Loading)
    val searchStoreModelData: StateFlow<UiState<List<StoreDetail>>> =
        _searchStoreModelData.asStateFlow()

    private val _searchBounds =
        MutableStateFlow(Pair(LatLng(0.0, 0.0), LatLng(0.0, 0.0)))
    val searchBounds: StateFlow<Pair<LatLng, LatLng>> =
        _searchBounds.asStateFlow()

    private val _mapCenterCoordinate = MutableStateFlow(Coordinate(0.0, 0.0))
    val mapCenterCoordinate: StateFlow<Coordinate> = _mapCenterCoordinate

    private val _mapZoomLevel = MutableStateFlow(0.0)
    val mapZoomLevel: StateFlow<Double> = _mapZoomLevel

    fun showMoreStore(count: Int) {
        val newItem: List<StoreDetail> = when (val uiState = _storeDetailModelData.value) {
            is UiState.Success -> uiState.data.getOrNull(count) ?: emptyList()
            else -> emptyList()
        }
        val currentList = _flattenedStoreDetailList.value.toMutableList()
        newItem.forEach { currentList.add(it) }
        _flattenedStoreDetailList.value = currentList.toList()
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
            _flattenedStoreDetailList.value = emptyList()
            if (_isInitialize.value) {
                _flattenedStoreDetailList.value = result.data?.flatten() ?: emptyList()
            } else {
                showMoreStore(0)
            }
        }
    }

    fun updateStoreInitializeState(state: Int) {
        _storeInitializeState.value = state
    }

    fun updateIsInitialize() {
        _isInitialize.value = false
    }

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
                    setSearchBounds(result)
                }

                is Resource.Failure -> {
                    _searchStoreModelData.value =
                        UiState.Failure(result.message ?: FAIL_TO_LOAD_DATA)
                }

                is Resource.Loading -> {
                    _searchStoreModelData.value = UiState.Loading
                }
            }
            _flattenedStoreDetailList.value = result.data ?: emptyList()
        }
    }

    private fun setSearchBounds(result: Resource<List<StoreDetail>>) {
        if (result.data!!.size == 1) {
            _searchBounds.value = Pair(
                LatLng(0.0, 0.0),
                LatLng(result.data!![0].location.latitude, result.data!![0].location.longitude)
            )
        } else {
            val longitudeValues = result.data!!.map { it.location.longitude }
            val latitudeValues = result.data!!.map { it.location.latitude }
            val eastValue = longitudeValues.max()
            val westValue = longitudeValues.min()
            val northValue = latitudeValues.max()
            val southValue = latitudeValues.min()
            _searchBounds.value = Pair(
                LatLng(southValue, westValue),
                LatLng(northValue, eastValue)
            )
        }
    }

    fun updateMapCenterCoordinate(coordinate: Coordinate) {
        _mapCenterCoordinate.value = coordinate
    }

    fun updateMapZoomLevel(zoom: Double) {
        _mapZoomLevel.value = zoom
    }
}