package com.example.presentation.ui.map.reload

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.White

@Composable
fun ShowMoreButton(
    showMoreCount: Pair<Int, Int>,
    onShowMoreCountChanged: (Pair<Int, Int>) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    Button(
        onClick = {
            if (showMoreCount.first + 1 < showMoreCount.second) {
                viewModel.showMoreStore(showMoreCount.first + 1)
                onShowMoreCountChanged(Pair(showMoreCount.first + 1, showMoreCount.second))
            }
        },
        modifier = Modifier
            .size(width = 110.dp, height = 35.dp),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black
        ),
        shape = RoundedCornerShape(30.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "결과 더보기 ${showMoreCount.first + 1}/${showMoreCount.second}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (showMoreCount.first == showMoreCount.second - 1) Gray else Black
        )
    }
}
