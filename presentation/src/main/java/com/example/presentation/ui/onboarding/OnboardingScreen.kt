package com.example.presentation.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        val pageCount = 5
        val pagerState = rememberPagerState(pageCount = { pageCount })
        HorizontalPager(
            beyondBoundsPageCount = 2,
            state = pagerState
        ) {
            PagerItem(page = it)
        }
        Row(
            Modifier
                .height(106.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MediumBlue else SemiLightGray
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
fun PagerItem(page: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        Text(
            text = "페이지:$page",
            color = Black
        )
        if (page == 4) {
            Button(
                colors = ButtonDefaults.buttonColors(MediumBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 15.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                }
            ) {
                Text(
                    text = "시작하기",
                    color = White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview
fun greetingPreview() {
    OnboardingScreen()
}