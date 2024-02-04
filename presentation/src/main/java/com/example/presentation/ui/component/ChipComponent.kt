package com.example.presentation.ui.component

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.model.StoreType
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.LightYellow
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Pink


@Composable
fun StoreTypeChip(storeType: StoreType) {
    Surface(
        color = when (storeType) {
            StoreType.KIND -> Pink
            StoreType.GREAT -> LightYellow
            StoreType.SAFE -> LightBlue
        },
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = stringResource(storeType.storeTypeName),
            color = MediumGray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoreTypeChips(elements: List<StoreType>, id: String) {
    FlowRow(
        modifier = Modifier
            .layoutId(id)
    ) {
        elements.forEach { item ->
            StoreTypeChip(storeType = item)
        }
    }
}