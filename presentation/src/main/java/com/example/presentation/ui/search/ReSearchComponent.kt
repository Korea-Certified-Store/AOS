package com.example.presentation.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.map.reload.LoadingAnimation
import com.example.presentation.ui.map.reload.setReloadButtonBottomPadding
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.UN_MARKER

@Composable
fun ReSearchComponent(
    isMarkerClicked: Boolean,
    currentSummaryInfoHeight: Dp,
    isMapGestured: Boolean,
    onReSearchButtonChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(
                bottom = setReloadButtonBottomPadding(
                    isMarkerClicked,
                    currentSummaryInfoHeight
                )
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isMapGestured) {
            ReSearchButton(
                onReSearchButtonChanged,
                onMarkerChanged,
                onBottomSheetChanged,
                isLoading,
            )
        }
    }
}


@Composable
fun ReSearchButton(
    onReSearchButtonChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    isLoading: Boolean,
    viewModel: MapViewModel = hiltViewModel()
) {
    Button(
        onClick = {
            onMarkerChanged(UN_MARKER)
            onBottomSheetChanged(false)
            onReSearchButtonChanged(false)
        },
        modifier = Modifier
            .size(width = 145.dp, height = 35.dp),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        ),
        shape = RoundedCornerShape(30.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            if (isLoading) {
                LoadingAnimation()
            } else {
                ReSearchByKeyword()
            }
        }
    }
}

@Composable
private fun ReSearchByKeyword() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.reload),
            tint = Blue,
            contentDescription = "ReSearch",
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.search_by_keyword_on_current_map),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}