package com.example.presentation.ui.map.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.mapper.toUiModel
import com.example.presentation.model.ExpandedType
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.component.BottomSheetDragHandle
import com.example.presentation.ui.component.EmptyScreen
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_EXPAND_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_FULL_PADDING
import com.example.presentation.util.UiState
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
    onListItemChanged: (Boolean) -> Unit,
    mapViewModel: MapViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            StoreListHeader(mapViewModel)
            StoreListContent(
                onBottomSheetChanged,
                onStoreInfoChanged,
                onMarkerChanged,
                onListItemChanged,
                mapViewModel
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

@Composable
fun StoreListHeader(viewModel: MapViewModel) {
    val storeDetailData by viewModel.flattenedStoreDetailList.collectAsStateWithLifecycle()
    val uiState by viewModel.storeDetailModelData.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 9.dp, bottom = 17.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.gather_the_stores),
            color = Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = if (uiState is UiState.Loading) ""
            else {
                if (storeDetailData.isEmpty()) stringResource(R.string.no_search_result)
                else stringResource(R.string.have_n_stores, storeDetailData.size)
            },
            color = DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StoreListContent(
    onBottomSheetChanged: (Boolean) -> Unit,
    onStoreInfoChanged: (StoreDetail) -> Unit,
    onMarkerChanged: (Long) -> Unit,
    onListItemChanged: (Boolean) -> Unit,
    viewModel: MapViewModel
) {
    val storeDetailData by viewModel.flattenedStoreDetailList.collectAsStateWithLifecycle()

    Column(modifier = Modifier.height(LIST_BOTTOM_SHEET_EXPAND_HEIGHT.dp)) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.5.dp, color = SemiLightGray
        )
        if (storeDetailData.isEmpty()) {
            EmptyScreen(R.string.can_not_find_stores)
        } else {
            LazyColumn {
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
    }
}