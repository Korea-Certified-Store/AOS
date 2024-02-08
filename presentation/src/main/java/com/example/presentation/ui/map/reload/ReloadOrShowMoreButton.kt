package com.example.presentation.ui.map.reload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.domain.model.map.ShowMoreCount

@Composable
fun ReloadOrShowMoreButton(
    isMarkerClicked: Boolean,
    currentSummaryInfoHeight: Dp,
    isMapGestured: Boolean,
    onShowMoreCountChanged: (ShowMoreCount) -> Unit,
    onReloadButtonChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    isLoading: Boolean,
    showMoreCount: ShowMoreCount
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(
                bottom = setReloadButtonBottomPadding(
                    isMarkerClicked,
                    currentSummaryInfoHeight
                )
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isMapGestured) {
            ReloadButton(
                onReloadButtonChanged,
                onMarkerChanged,
                onBottomSheetChanged,
                isLoading,
            )
        } else {
            ShowMoreButton(showMoreCount, onShowMoreCountChanged)
        }
    }
}