package com.example.presentation.ui.map.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.example.presentation.model.ExpandedType
import com.example.presentation.ui.theme.BackgroundBlack

@Composable
fun DimScreen(onBottomSheetExpandedChanged: (ExpandedType) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .pointerInput(Unit) { detectTapGestures {} }
            .noRippleClickable {
                onBottomSheetExpandedChanged(ExpandedType.DIM)
            }
    )
}

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit = {},
): Modifier = composed {
    this.clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}