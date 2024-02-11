package com.example.presentation.ui.map.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.component.BottomSheetDragHandle
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_EXPAND_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_FULL_PADDING
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListBottomSheet(
    bottomSheetExpandedType: ExpandedType,
    onBottomSheetExpandedChanged: (ExpandedType) -> Unit,
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onListItemChanged: (Boolean) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            StoreListHeader()
            StoreListContent(
                onBottomSheetChanged,
                onStoreInfoChanged,
                onMarkerChanged,
                onListItemChanged
            )
        },
        sheetPeekHeight = (LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT + HANDLE_HEIGHT).dp,
        sheetContainerColor = White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = { BottomSheetDragHandle() }
    ) {

        val density = LocalDensity.current
        var bottomSheetHeight = (LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT + HANDLE_HEIGHT).dp

        runCatching {
            bottomSheetHeight = with(density) {
                screenHeight.dp - scaffoldState.bottomSheetState.requireOffset()
                    .toDp() - HANDLE_HEIGHT.dp
            }
        }

        if (bottomSheetHeight > (LIST_BOTTOM_SHEET_FULL_PADDING + LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT + HANDLE_HEIGHT).dp) {
            onBottomSheetExpandedChanged(ExpandedType.FULL)
        } else if (bottomSheetHeight > (LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT + HANDLE_HEIGHT).dp) {
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

@Composable
fun StoreListContent(
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onListItemChanged: (Boolean) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val storeDetailData by viewModel.flattenedStoreDetailList.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.heightIn(max = LIST_BOTTOM_SHEET_EXPAND_HEIGHT.dp)) {
        itemsIndexed(
            storeDetailData.filter {
                viewModel.getFilterSet().intersect(it.certificationName.toSet())
                    .isNotEmpty()
            }
        ) { _, item ->
            StoreListItem(
                item.toUiModel(),
                onBottomSheetChanged,
                onStoreInfoChanged,
                onMarkerChanged,
                onListItemChanged
            )
            StoreListDivider()
        }
    }
}