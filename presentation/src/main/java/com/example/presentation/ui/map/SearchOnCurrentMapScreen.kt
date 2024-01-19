package com.example.presentation.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_ON
import com.example.presentation.util.MainConstants.SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOnCurrentMapButton(
    isMarkerClicked: Boolean,
    onSearchOnCurrentMapButtonChanged: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(bottom = setSearchOnCurrentMapBottomPadding(isMarkerClicked)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onSearchOnCurrentMapButtonChanged(true) },
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 11.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Blue
                ),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.search),
                    tint = Blue,
                    contentDescription = "Search",
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.search_on_current_map),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

fun setSearchOnCurrentMapBottomPadding(isMarkerClicked: Boolean): Dp {
    return if (isMarkerClicked) (BOTTOM_SHEET_HEIGHT_ON + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING).dp
    else (BOTTOM_SHEET_HEIGHT_OFF + SEARCH_ON_CURRENT_MAP_BUTTON_DEFAULT_PADDING).dp
}