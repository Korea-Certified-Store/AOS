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

@Composable
fun ReloadOrShowMoreButton(
    isMarkerClicked: Boolean,
    currentSummaryInfoHeight: Dp,
    isMapGestured: Boolean,
    onShowMoreCountChanged: (Pair<Int, Int>) -> Unit,
    onReloadButtonChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    isLoading: Boolean,
    showMoreCount: Pair<Int, Int>
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