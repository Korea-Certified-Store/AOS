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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
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
fun StoreSearchComponent(
    navController: NavController,
    searchText: String?,
    onSearchComponentChanged: (Boolean) -> Unit
) {
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
                onSearchComponentChanged(true)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (searchText.isNullOrBlank()) {
            SearchPlaceHolderText(stringResource(R.string.search_placeholder_text), MediumGray)
            SearchSuffixImage(R.drawable.search, navController)
        } else {
            SearchPlaceHolderText(searchText, Black)
            SearchSuffixImage(R.drawable.delete, navController)
        }
    }
}

@Composable
fun SearchPlaceHolderText(text: String, textColor: Color) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = textColor,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = DEFAULT_MARGIN.dp),
        maxLines = 1
    )
}

@Composable
fun SearchSuffixImage(@DrawableRes image: Int, navController: NavController) {
    Image(
        imageVector = ImageVector.vectorResource(id = image),
        contentDescription = "Delete",
        modifier = Modifier
            .padding(end = DEFAULT_MARGIN.dp)
            .size(16.dp)
            .clickable(enabled = image == R.drawable.delete) {
                navController.navigate(Screen.Main.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
    )
}