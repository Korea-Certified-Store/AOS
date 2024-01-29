package com.example.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.R

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // xml에서의 match_parent와 같은 의미입니다.
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "나인가 로고",
            alignment = Alignment.Center
        )
    }
}

@Preview
@Composable
fun PreviewSplashBox() {
    SplashScreen()
}