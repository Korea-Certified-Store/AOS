package com.example.presentation.ui.onboarding

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
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
            val pageContent = getContentByPageNumber(it)
            PagerItem(pageContent, page = it)
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
        "인증제 별 가게 위치를\n지도로 한눈에 알아봐요!",
        R.drawable.onboarding_first,
        "여기서 잠깐,\n인증제에 대해 설명해 드릴게요!"
    )

    1 ->
        OnboardingPageContent(
            "착한 가격 업소란?\n",
            R.drawable.onboarding_second,
            "\n\n2011년부터 물가안정을 위해\n가격이 저렴하지만 양질의 서비스를\n제공하는 곳을 정부가 지정한\n우리 동네의 좋은 업소입니다."
        )

    2 ->
        OnboardingPageContent(
            "모범 음식점이란?\n",
            R.drawable.onboarding_third,
            "\n식품위생법에 근거하여\n위생관리 상태 등이 우수한 업소를\n모범업소로 지정합니다.\n서비스 수준 향상과 위생적 개선을 도모하기\n위해 운영되고 있습니다."
        )

    3 ->
        OnboardingPageContent(
            "안심식당이란?\n",
            R.drawable.onboarding_fourth,
            "\n\n감염병에 취약한 식사문화 개선을 위해\n덜어 먹기, 위생적 수저관리, 종사자 마스크\n착용 및 생활 방역을 준수하는 곳으로\n소재지 지자체의 인증을\n받은 음식점을 의미합니다."
        )

    4 ->
        OnboardingPageContent(
            "나인가 시작하기\n",
            R.drawable.onboarding_fifth,
            "\n이젠 정말 나인가와 함께 할 시간이에요!"
        )

    else -> OnboardingPageContent("", 0, "")
}

@Composable
fun PagerItem(
    pageContent: OnboardingPageContent,
    page: Int
) {
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
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(pageContent.image),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .width(224.dp)
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
                colors = ButtonDefaults.buttonColors(MediumBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Indicator(
    pageCount: Int,
    pagerState: PagerState
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