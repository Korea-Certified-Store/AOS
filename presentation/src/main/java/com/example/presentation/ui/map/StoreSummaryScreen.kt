package com.example.presentation.ui.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.LightYellow
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Pink
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    heightType: Int,
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(clickedStoreInfo, onCallDialogChanged)
            }
        },
        sheetPeekHeight = heightType.dp,
        sheetContainerColor = White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .width(32.dp)
                        .height(3.dp)
                        .background(Color.LightGray)
                )
            }
        },
        sheetSwipeEnabled = false
    ) {

    }
}

@Composable
fun StoreSummaryInfo(
    storeInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            StoreTitle(storeInfo.displayName, storeInfo.primaryTypeDisplayName?:"상점")
            Spacer(modifier = Modifier.height(8.dp))
            Chips(storeInfo.certificationName)
            Spacer(modifier = Modifier.height(8.dp))
            StoreOpeningTime()
//            StoreOpeningTime(storeInfo.regularOpeningHours)
            Spacer(modifier = Modifier.height(11.dp))
            StoreCallButton(onCallDialogChanged)
            Spacer(modifier = Modifier.height(12.dp))
        }
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            StoreImage()
        }
    }
}

@Composable
fun StoreTitle(storeName: String, storeType: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = storeName,
            color = MediumBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = storeType,
            color = MediumGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun Chip(
    storeType: StoreType
) {
    Surface(
        color = when (storeType) {
            StoreType.KIND -> Pink
            StoreType.GREAT -> LightYellow
            StoreType.SAFE -> LightBlue
        },
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(
            text = stringResource(storeType.storeTypeName),
            color = MediumGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Thin,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun Chips(
    elements: List<StoreType>
) {
    LazyRow(modifier = Modifier) {
        items(elements.size) { idx ->
            Chip(storeType = elements[idx])
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
fun StoreOpeningTime() {
    Row {
        Text(
            text = "영업 중",
            Modifier.alignByBaseline(),
            color = Red,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "openingHours",
            Modifier.alignByBaseline(),
            color = MediumGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreCallButton(onCallDialogChanged: (Boolean) -> Unit) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            onClick = {
                onCallDialogChanged(true)
            },
            modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = White),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.call),
                tint = DarkGray,
                contentDescription = "Call",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun StoreImage() {
    Card(
        modifier = Modifier.size(116.dp),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.3.dp, LightGray)
    ) {
        Image(
            painter = painterResource(R.drawable.store_example),
            contentDescription = "Store Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}