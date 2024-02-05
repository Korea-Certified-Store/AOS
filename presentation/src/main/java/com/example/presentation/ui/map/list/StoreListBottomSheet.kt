package com.example.presentation.ui.map.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListBottomSheet() {
    BottomSheetScaffold(
        sheetContent = {
            StoreListHeader()
        },
        sheetPeekHeight = (38 + HANDLE_HEIGHT).dp,
        sheetContainerColor = White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = {
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
    ) {

    }
}

@Preview
@Composable
fun StoreListHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 9.dp, bottom = 14.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.gather_the_stores),
            color = Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}