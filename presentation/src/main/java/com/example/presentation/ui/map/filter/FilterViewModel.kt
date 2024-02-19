package com.example.presentation.ui.map.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    private val _isKindFilterClicked = MutableStateFlow(false)
    val isKindFilterClicked: StateFlow<Boolean> get() = _isKindFilterClicked

    private val _isGreatFilterClicked = MutableStateFlow(false)
    val isGreatFilterClicked: StateFlow<Boolean> get() = _isGreatFilterClicked

    private val _isSafeFilterClicked = MutableStateFlow(false)
    val isSafeFilterClicked: StateFlow<Boolean> get() = _isSafeFilterClicked

    fun updateKindFilterClicked() {
        _isKindFilterClicked.value = _isKindFilterClicked.value.not()
    }

    fun updateGreatFilterClicked() {
        _isGreatFilterClicked.value = _isGreatFilterClicked.value.not()
    }

    fun updateSafeFilterClicked() {
        _isSafeFilterClicked.value = _isSafeFilterClicked.value.not()
    }

    fun updateAllFilterUnClicked() {
        _isKindFilterClicked.value = false
        _isGreatFilterClicked.value = false
        _isSafeFilterClicked.value = false
        Log.d("테스트","${_isKindFilterClicked.value}")
    }
}