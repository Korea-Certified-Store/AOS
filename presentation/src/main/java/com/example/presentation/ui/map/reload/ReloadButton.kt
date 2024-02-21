package com.example.presentation.ui.map.reload

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.presentation.R
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT
import com.example.presentation.util.MainConstants.RELOAD_BUTTON_DEFAULT_PADDING
import com.example.presentation.util.MainConstants.UN_MARKER

@Composable
fun ReloadButton(
    onReloadButtonChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    isLoading: Boolean,
    viewModel: MapViewModel
) {
    Button(
        onClick = {
            if (viewModel.isInitialize.value) {
                viewModel.updateIsInitialize()
            }
            onReloadButtonChanged(true)
            onMarkerChanged(UN_MARKER)
            onBottomSheetChanged(false)
        },
        modifier = Modifier
            .size(width = 125.dp, height = 35.dp),
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
                ReloadInCurrentLocation()
            }
        }
    }
}

@Composable
private fun ReloadInCurrentLocation() {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.reload),
        tint = Blue,
        contentDescription = "Reload",
        modifier = Modifier.size(13.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
        text = stringResource(R.string.search_on_current_map),
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
    )
}

fun setReloadButtonBottomPadding(isMarkerClicked: Boolean, bottomSheetHeight: Dp): Dp {
    return if (isMarkerClicked) bottomSheetHeight + (RELOAD_BUTTON_DEFAULT_PADDING + HANDLE_HEIGHT).dp
    else (RELOAD_BUTTON_DEFAULT_PADDING + HANDLE_HEIGHT + LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT).dp
}