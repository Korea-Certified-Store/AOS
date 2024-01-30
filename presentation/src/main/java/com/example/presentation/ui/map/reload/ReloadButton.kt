package com.example.presentation.ui.map.reload

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
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import com.example.presentation.util.MainConstants.RELOAD_BUTTON_DEFAULT_PADDING
import com.example.presentation.util.MainConstants.UNMARKER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReloadButton(
    isMarkerClicked: Boolean,
    onReloadButtonChanged: (Boolean) -> Unit,
    bottomSheetHeight: Dp,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(
                    bottom = setReloadButtonBottomPadding(
                        isMarkerClicked,
                        bottomSheetHeight
                    )
                ),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    onReloadButtonChanged(true)
                    onMarkerChanged(UNMARKER)
                    onBottomSheetChanged(false)
                },
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = Black
                ),
                shape = RoundedCornerShape(30.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

fun setReloadButtonBottomPadding(isMarkerClicked: Boolean, bottomSheetHeight: Dp): Dp {
    return if (isMarkerClicked) bottomSheetHeight + (RELOAD_BUTTON_DEFAULT_PADDING + HANDLE_HEIGHT).dp
    else RELOAD_BUTTON_DEFAULT_PADDING.dp
}