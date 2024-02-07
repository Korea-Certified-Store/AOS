package com.example.presentation.ui.onboarding

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.SemiDarkBlue
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.ONBOARDING_EXPLANATION_1
import com.example.presentation.util.MainConstants.ONBOARDING_EXPLANATION_2
import com.example.presentation.util.MainConstants.ONBOARDING_EXPLANATION_3
import com.example.presentation.util.MainConstants.ONBOARDING_EXPLANATION_4
import com.example.presentation.util.MainConstants.ONBOARDING_EXPLANATION_5
import com.example.presentation.util.MainConstants.ONBOARDING_TITLE_1
import com.example.presentation.util.MainConstants.ONBOARDING_TITLE_2
import com.example.presentation.util.MainConstants.ONBOARDING_TITLE_3
import com.example.presentation.util.MainConstants.ONBOARDING_TITLE_4
import com.example.presentation.util.MainConstants.ONBOARDING_TITLE_5


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onOnboardingScreenShowAble: (Boolean) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        val pageCount = 5
        val pagerState = rememberPagerState(pageCount = { pageCount })
        HorizontalPager(
            state = pagerState
        ) {
            val pageContent = getContentByPageNumber(it)
            PagerItem(pageContent, page = it, onOnboardingScreenShowAble)
        }
        Row(
            Modifier
                .height(106.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            Indicator(pageCount, pagerState)
        }
    }
}

@Composable
private fun getContentByPageNumber(it: Int) = when (it) {
    0 -> OnboardingPageContent(
        ONBOARDING_TITLE_1,
        R.drawable.onboarding_first,
        ONBOARDING_EXPLANATION_1
    )

    1 ->
        OnboardingPageContent(
            ONBOARDING_TITLE_2,
            R.drawable.onboarding_second,
            ONBOARDING_EXPLANATION_2
        )

    2 ->
        OnboardingPageContent(
            ONBOARDING_TITLE_3,
            R.drawable.onboarding_third,
            ONBOARDING_EXPLANATION_3
        )

    3 ->
        OnboardingPageContent(
            ONBOARDING_TITLE_4,
            R.drawable.onboarding_fourth,
            ONBOARDING_EXPLANATION_4
        )

    4 ->
        OnboardingPageContent(
            ONBOARDING_TITLE_5,
            R.drawable.onboarding_fifth,
            ONBOARDING_EXPLANATION_5
        )

    else -> OnboardingPageContent("", 0, "")
}

@Composable
fun PagerItem(
    pageContent: OnboardingPageContent,
    page: Int,
    onOnboardingScreenShowAble: (Boolean) -> Unit
) {

    val context = LocalContext.current
    val preferences = context.getSharedPreferences("KCS_Onboarding", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 70.dp),
            text = pageContent.title,
            color = SemiDarkBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(pageContent.image),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .width(210.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 60.dp),
            contentScale = ContentScale.FillWidth,
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 40.dp),
            text = pageContent.explanation,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        if (page == 4) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(SemiDarkBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 15.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = {
                    onOnboardingScreenShowAble(false)
                    with(preferences.edit()) {
                        putBoolean("isFirstRun", false)
                        apply()
                    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Indicator(
    pageCount: Int,
    pagerState: PagerState
) {
    repeat(pageCount) { iteration ->
        val color =
            if (pagerState.currentPage == iteration) SemiDarkBlue else SemiLightGray
        Box(
            modifier = Modifier
                .padding(3.dp)
                .clip(CircleShape)
                .background(color)
                .size(10.dp)
        )
    }
}