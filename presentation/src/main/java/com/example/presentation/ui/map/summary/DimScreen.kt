package com.example.presentation.ui.map.summary

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import com.example.presentation.ui.theme.Transparent
import com.example.presentation.util.MainConstants.DIM_ANIMATION_MILLIS

@Composable
fun DimScreen(
    bottomSheetExpandedType: ExpandedType,
    onBottomSheetExpandedChanged: (ExpandedType) -> Unit
) {
    val animatedColor = animateColorAsState(
        if (bottomSheetExpandedType == ExpandedType.FULL) BackgroundBlack else Transparent,
        animationSpec = tween(DIM_ANIMATION_MILLIS, easing = LinearEasing), label = "dimColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedColor.value)
            .pointerInput(Unit) { detectTapGestures {} }
            .noRippleClickable {
                onBottomSheetExpandedChanged(ExpandedType.DIM_CLICK)
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