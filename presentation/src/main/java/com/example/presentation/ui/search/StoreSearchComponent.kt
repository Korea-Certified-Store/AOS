package com.example.presentation.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.presentation.R
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_HEIGHT
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_TOP_PADDING

@Composable
fun StoreSearchComponent(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(
                start = DEFAULT_MARGIN.dp,
                end = DEFAULT_MARGIN.dp,
                top = SEARCH_TEXT_FIELD_TOP_PADDING.dp,
                bottom = 8.dp
            )
            .shadow(4.dp, RoundedCornerShape(size = 12.dp))
            .fillMaxWidth()
            .height(SEARCH_TEXT_FIELD_HEIGHT.dp)
            .background(color = White, shape = RoundedCornerShape(size = 12.dp))
            .clickable { navController.navigate(Screen.Search.route) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.search_placeholder_text),
            fontSize = 14.sp,
            color = SemiLightGray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = DEFAULT_MARGIN.dp)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.search),
            contentDescription = "Search",
            modifier = Modifier
                .padding(end = DEFAULT_MARGIN.dp)
                .size(16.dp)
        )
    }
}