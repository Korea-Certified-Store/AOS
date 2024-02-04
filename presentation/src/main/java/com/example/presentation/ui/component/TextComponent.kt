package com.example.presentation.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray

@Composable
fun StoreTitleText(storeName: String, fontSize: Int, id: String) {
    Text(
        text = storeName,
        color = MediumBlue,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.layoutId(id)
    )
}

@Composable
fun StorePrimaryTypeText(storeType: String, fontSize: Int, id: String) {
    Text(
        text = storeType,
        color = MediumGray,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.layoutId(id)
    )
}