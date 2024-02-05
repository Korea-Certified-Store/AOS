package com.example.presentation.ui.map.list

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.R
import com.example.presentation.mapper.toUiModel
import com.example.presentation.ui.component.BottomSheetDragHandle
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.HANDLE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT
import com.example.presentation.util.MainConstants.LIST_BOTTOM_SHEET_EXPAND_HEIGHT
import com.example.presentation.util.UiState

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListBottomSheet() {
    BottomSheetScaffold(
        sheetContent = {
            StoreListHeader()
            StoreListContent()
        },
        sheetPeekHeight = (LIST_BOTTOM_SHEET_COLLAPSE_HEIGHT + HANDLE_HEIGHT).dp,
        sheetContainerColor = White,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetShadowElevation = 5.dp,
        sheetDragHandle = { BottomSheetDragHandle() }
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

@Composable
fun StoreListContent(viewModel: MapViewModel = hiltViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val storeDetailData by viewModel.storeDetailModelData.collectAsStateWithLifecycle(
        lifecycleOwner
    )

    LazyColumn(modifier = Modifier.heightIn(max = LIST_BOTTOM_SHEET_EXPAND_HEIGHT.dp)) {
        itemsIndexed(
            when (val state = storeDetailData) {
                is UiState.Success -> {
                    state.data.first().map { it.toUiModel() }
                }
                else -> {
                    emptyList()
                }
            }
        ) { _, item ->
            StoreListItem(storeInfo = item)
            StoreListDivider()
        }
    }
}