package com.example.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.ui.theme.SemiLightGray

@Composable
fun BottomSheetDragHandle() {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(4.dp)
                .background(SemiLightGray, shape = RoundedCornerShape(100.dp))
        )
    }
}