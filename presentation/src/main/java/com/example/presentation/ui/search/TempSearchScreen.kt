package com.example.presentation.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TempSearchScreen(
    onSearchButtonChanged: (Boolean) -> Unit,
) {
    Column {
        Spacer(modifier = Modifier.height(100.dp))
        Row {
            Spacer(modifier = Modifier.width(200.dp))
            Button(onClick = {
                onSearchButtonChanged(true)
            }) {
                Text(text = "테스트 용 버튼")
            }
        }
    }
}