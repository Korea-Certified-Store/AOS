package com.example.presentation.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.presentation.R

@Composable
fun SplashAnimation() {
    val loadingImgList = listOf(
        R.drawable.splash_1,
        R.drawable.splash_2,
        R.drawable.splash_3,
        R.drawable.splash_4,
        R.drawable.splash_5,
    )

    val imgIdx = remember {
        Animatable(initialValue = 0f)
    }
    val currentImg = loadingImgList[imgIdx.value.toInt()]

    LaunchedEffect(Unit) {
        imgIdx.animateTo(
            targetValue = (loadingImgList.lastIndex + 1).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    val rotation = animateFloatAsState(
        targetValue = angleAnimation(),
        animationSpec = tween(
            durationMillis = 500,
            easing = LinearEasing,
        ), label = ""
    )

    Image(
        painter = painterResource(id = currentImg),
        contentDescription = "loading",
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
    )
}

@Composable
fun angleAnimation(): Float {
    val angles = listOf(
        0f, 180f
    )

    val angleIdx = remember {
        Animatable(initialValue = 0f)
    }
    val currentAngle = angles[angleIdx.value.toInt()]

    LaunchedEffect(Unit) {
        angleIdx.animateTo(
            targetValue = (angles.lastIndex + 1).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    return currentAngle
}