package com.example.presentation.ui.map.summary

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.theme.BackgroundBlack
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DETAIL_BOTTOM_SHEET_HEIGHT
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit,
    currentSummaryInfoHeight: Dp,
    onCurrentSummaryInfoHeightChanged: (Dp) -> Unit,
    isStoreDetail: Boolean,
    onStoreDetailChanged: (Boolean) -> Unit
) {
    val bottomSheetSt = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetSt)

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(modifier = Modifier.height(DETAIL_BOTTOM_SHEET_HEIGHT.dp)) {
                if (isStoreDetail) {
                    TestDetail()
                } else {
                    StoreSummaryInfo(
                        clickedStoreInfo,
                        onCallDialogChanged,
                        onCurrentSummaryInfoHeightChanged,
                        currentSummaryInfoHeight
                    )
                }
            }
        },
        sheetPeekHeight = currentSummaryInfoHeight + HANDLE_HEIGHT.dp,
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
        val density = LocalDensity.current
        val bottomSheetHeight = with(density) {
            screenHeight.dp - scaffoldState.bottomSheetState.requireOffset()
                .toDp() - HANDLE_HEIGHT.dp
        }
        onStoreDetailChanged(bottomSheetHeight >= (DETAIL_BOTTOM_SHEET_HEIGHT.dp + currentSummaryInfoHeight) / 2)

        if (isStoreDetail.not()) {
            scope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }
    }
}

@Composable
fun DimScreen(onStoreDetailChanged: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack)
            .clickable { onStoreDetailChanged(false) }
    )
}

@Composable
fun TestDetail() {
    Text(text = "디테일 화면")
}