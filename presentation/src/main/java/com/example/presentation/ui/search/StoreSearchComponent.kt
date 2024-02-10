package com.example.presentation.ui.search

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_HEIGHT
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_TOP_PADDING

@Composable
fun StoreSearchComponent(navController: NavController, searchText: String?) {
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
            .border(
                border = BorderStroke(if (searchText == null) 0.dp else 1.5.dp, MediumGray),
                shape = RoundedCornerShape(size = 12.dp)
            )
            .clickable {
                if (searchText == null) {
                    navController.navigate(Screen.Search.route)
                } else {
                    navController.popBackStack()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (searchText == null) {
            SearchPlaceHolderText(stringResource(R.string.search_placeholder_text))
            SearchSuffixImage(R.drawable.search)
        } else {
            SearchPlaceHolderText(searchText)
            SearchSuffixImage(R.drawable.delete)
        }
    }
}

@Composable
fun SearchPlaceHolderText(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = MediumGray,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = DEFAULT_MARGIN.dp),
        maxLines = 1
    )
}

@Composable
fun SearchSuffixImage(@DrawableRes image: Int) {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.delete),
        contentDescription = "Delete",
        modifier = Modifier
            .padding(end = DEFAULT_MARGIN.dp)
            .size(16.dp)
    )
}