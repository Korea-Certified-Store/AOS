package com.example.presentation.ui.map.reload

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.presentation.R

@Composable
fun LoadingAnimation() {
    val loadingImgList = listOf(
        R.drawable.loading1,
        R.drawable.loading2,
        R.drawable.loading3,
        R.drawable.loading4,
        R.drawable.loading5
    )

    val imgIdx = remember {
        Animatable(initialValue = 0f)
    }
    val currentImg = loadingImgList[imgIdx.value.toInt()]

    LaunchedEffect(Unit) {
        imgIdx.animateTo(
            targetValue = (loadingImgList.lastIndex + 1).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Image(
        painter = painterResource(id = currentImg),
        contentDescription = "loading",
        modifier = Modifier.size(width = 36.dp, height = 13.dp)
    )
}