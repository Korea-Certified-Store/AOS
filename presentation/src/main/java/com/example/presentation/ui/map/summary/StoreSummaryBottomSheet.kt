package com.example.presentation.ui.map.summary

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.UNMARKER
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    isMarkerClicked: Boolean,
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit
) {
    val bottomSheetSt = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetSt)
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    var peekHeight by remember { mutableIntStateOf(0) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomSheetGestureWrapper(
                isMarkerClicked = isMarkerClicked,
                onExpandTypeChanged = {
                    scope.launch {
                        peekHeight = when (it) {
                            ExpandedType.COLLAPSED -> 0
                            ExpandedType.FULL -> 544
                            ExpandedType.HALF -> screenHeight / 2
                        }
                        bottomSheetSt.partialExpand() // Smooth animation to desired height
                    }
                },
                onMarkerChanged = onMarkerChanged,
                onBottomSheetChanged = onBottomSheetChanged
            ) {
                StoreSummaryInfo(
                    clickedStoreInfo,
                    onCallDialogChanged
                )
            }

        },
        sheetPeekHeight = peekHeight.dp,
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


@Composable
fun BottomSheetGestureWrapper(
    isMarkerClicked: Boolean,
    onExpandTypeChanged: (ExpandedType) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    var expandedType by remember {
        mutableStateOf(ExpandedType.COLLAPSED)
    }
    var changedDragAmount by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(key1 = expandedType) {
        if (expandedType == ExpandedType.COLLAPSED) {
            onMarkerChanged(UNMARKER)
            onBottomSheetChanged(false)
        }
        onExpandTypeChanged(expandedType)
    }

    LaunchedEffect(key1 = isMarkerClicked) {
        expandedType = if (isMarkerClicked) ExpandedType.HALF else ExpandedType.COLLAPSED
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(544.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        changedDragAmount = dragAmount
                    },
                    onDragEnd = {
                        expandedType = when {
                            changedDragAmount < 0 && expandedType == ExpandedType.COLLAPSED -> {
                                ExpandedType.HALF
                            }

                            changedDragAmount < 0 && expandedType == ExpandedType.HALF -> {
                                ExpandedType.FULL
                            }

                            changedDragAmount > 0 && expandedType == ExpandedType.FULL -> {
                                ExpandedType.HALF
                            }

                            changedDragAmount > 0 && expandedType == ExpandedType.HALF -> {
                                ExpandedType.COLLAPSED
                            }

                            else -> {
                                expandedType
                            }
                        }
                    }
                )
            }
    ) {
        content()
    }
}