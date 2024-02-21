package com.example.presentation.ui.map.filter

import androidx.lifecycle.ViewModel
import com.example.presentation.util.MainConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    private val _isFilteredMarker = MutableStateFlow(false)
    val isFilteredMarker: StateFlow<Boolean> = _isFilteredMarker

    private val filterSet = mutableSetOf<String>()

    private val _isKindFilterClicked = MutableStateFlow(false)
    val isKindFilterClicked: StateFlow<Boolean> get() = _isKindFilterClicked

    private val _isGreatFilterClicked = MutableStateFlow(false)
    val isGreatFilterClicked: StateFlow<Boolean> get() = _isGreatFilterClicked

    private val _isSafeFilterClicked = MutableStateFlow(false)
    val isSafeFilterClicked: StateFlow<Boolean> get() = _isSafeFilterClicked

    fun getFilterSet(): Set<String> {
        return if (filterSet.isEmpty()) setOf(
            MainConstants.SAFE_STORE,
            MainConstants.GREAT_STORE,
            MainConstants.KIND_STORE
        )
        else filterSet.toSet()
    }

    fun updateFilterSet(certificationName: String, isClicked: Boolean) {
        if (isClicked) {
            filterSet.add(certificationName)
        } else {
            filterSet.remove(certificationName)
        }
    }

    fun updateAllFilterUnClicked() {
        filterSet.clear()
        _isKindFilterClicked.value = false
        _isGreatFilterClicked.value = false
        _isSafeFilterClicked.value = false
    }

    fun updateKindFilterClicked() {
        _isKindFilterClicked.value = _isKindFilterClicked.value.not()
    }

    fun updateGreatFilterClicked() {
        _isGreatFilterClicked.value = _isGreatFilterClicked.value.not()
    }

    fun updateSafeFilterClicked() {
        _isSafeFilterClicked.value = _isSafeFilterClicked.value.not()
    }

    fun updateIsFilteredMarker(isFilteredMarker: Boolean) {
        _isFilteredMarker.value = isFilteredMarker
    }
}