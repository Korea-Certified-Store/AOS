package com.example.presentation.ui.map.summary

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.detail.StoreDetailInfo
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DETAIL_BOTTOM_SHEET_HEIGHT
import com.example.presentation.util.MainConstants.DIM_MARGIN
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
    bottomSheetExpandedType: ExpandedType,
    onBottomSheetExpandedChanged: (ExpandedType) -> Unit
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
            SetBottomSheetContent(
                bottomSheetExpandedType,
                clickedStoreInfo,
                onCallDialogChanged,
                onCurrentSummaryInfoHeightChanged,
                currentSummaryInfoHeight
            )
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

        if (bottomSheetHeight >= (DETAIL_BOTTOM_SHEET_HEIGHT.dp + currentSummaryInfoHeight) / 2 + DIM_MARGIN.dp) {
            onBottomSheetExpandedChanged(ExpandedType.FULL)
        } else if (bottomSheetHeight >= (DETAIL_BOTTOM_SHEET_HEIGHT.dp + currentSummaryInfoHeight) / 2) {
            onBottomSheetExpandedChanged(ExpandedType.DIM)
        } else if (bottomSheetHeight >= currentSummaryInfoHeight) {
            onBottomSheetExpandedChanged(ExpandedType.HALF)
        } else {
            onBottomSheetExpandedChanged(ExpandedType.COLLAPSED)
        }

        if (bottomSheetExpandedType == ExpandedType.DIM_CLICK) {
            scope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }
    }
}

@Composable
private fun SetBottomSheetContent(
    bottomSheetExpandedType: ExpandedType,
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit,
    onCurrentSummaryInfoHeightChanged: (Dp) -> Unit,
    currentSummaryInfoHeight: Dp
) {
    Box(modifier = Modifier.height(DETAIL_BOTTOM_SHEET_HEIGHT.dp)) {
        androidx.compose.animation.AnimatedVisibility(
            visible = bottomSheetExpandedType == ExpandedType.FULL || bottomSheetExpandedType == ExpandedType.DIM || bottomSheetExpandedType == ExpandedType.DIM_CLICK,
            enter = fadeIn(animationSpec = tween(durationMillis = 400)),
            exit = fadeOut(animationSpec = tween(durationMillis = 400)),
        ) {
            StoreDetailInfo(
                clickedStoreInfo
            )
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = !(bottomSheetExpandedType == ExpandedType.FULL || bottomSheetExpandedType == ExpandedType.DIM || bottomSheetExpandedType == ExpandedType.DIM_CLICK),
            enter = fadeIn(animationSpec = tween(durationMillis = 400)),
            exit = fadeOut(animationSpec = tween(durationMillis = 400)),
        ) {

            StoreSummaryInfo(
                clickedStoreInfo,
                onCallDialogChanged,
                onCurrentSummaryInfoHeightChanged,
                currentSummaryInfoHeight
            )

        }
    }
}