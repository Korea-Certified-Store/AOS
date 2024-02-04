package com.example.presentation.ui.map.list

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.presentation.model.Coordinate
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.ui.component.StoreImageCard
import com.example.presentation.ui.component.StoreTitleText
import com.example.presentation.ui.component.StorePrimaryTypeText
import com.example.presentation.ui.component.StoreTypeChips
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_STORE_LIST_IMG_SIZE
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN

@Preview
@Composable
fun test() {
    LazyColumn {
        itemsIndexed(
            listOf(
                StoreDetail(
                    id = 0,
                    displayName = "성산일출봉 청운식당Cheongwon",
                    primaryTypeDisplayName = "일본 음식점",
                    formattedAddress = "주소",
                    phoneNumber = "전화",
                    location = Coordinate(latitude = 0.0, longitude = 0.0),
                    operatingType = "타입",
                    timeDescription = "타임",
                    localPhotos = listOf(),
                    certificationName = listOf(StoreType.GREAT, StoreType.KIND, StoreType.SAFE),
                    operationTimeOfWeek = mapOf()

                ),
                StoreDetail(
                    id = 0,
                    displayName = "성산일출봉 청운식당Cheongwon",
                    primaryTypeDisplayName = "일본 음식점",
                    formattedAddress = "주소",
                    phoneNumber = "전화",
                    location = Coordinate(latitude = 0.0, longitude = 0.0),
                    operatingType = "타입",
                    timeDescription = "타임",
                    localPhotos = listOf(),
                    certificationName = listOf(StoreType.GREAT, StoreType.KIND, StoreType.SAFE),
                    operationTimeOfWeek = mapOf()

                ),
                StoreDetail(
                    id = 0,
                    displayName = "성산일출봉 청운식당Cheongwon",
                    primaryTypeDisplayName = "일본 음식점",
                    formattedAddress = "주소",
                    phoneNumber = "전화",
                    location = Coordinate(latitude = 0.0, longitude = 0.0),
                    operatingType = "타입",
                    timeDescription = "타임",
                    localPhotos = listOf(),
                    certificationName = listOf(StoreType.GREAT, StoreType.KIND, StoreType.SAFE),
                    operationTimeOfWeek = mapOf()
                )

            )
        ) { idx, item ->
            StoreListItem(storeInfo = item)
            StoreListDivider()
        }
    }
}


@Composable
fun StoreListItem(
    storeInfo: StoreDetail
) {
    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = DEFAULT_MARGIN.dp, vertical = DEFAULT_MARGIN.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            constraintSet = setBottomSheetConstraints()
        ) {
            StoreTitleText(storeInfo.displayName, 18, "storeTitle")
            StorePrimaryTypeText(storeInfo.primaryTypeDisplayName ?: "상점", 9, "storePrimaryType")
            StoreTypeChips(storeInfo.certificationName, "chips")
            StoreImageCard(storeInfo.localPhotos, BOTTOM_SHEET_STORE_LIST_IMG_SIZE, "storeImage")
        }
    }
}

fun setBottomSheetConstraints(): ConstraintSet {
    return ConstraintSet {
        val storeTitle = createRefFor("storeTitle")
        val storePrimaryType = createRefFor("storePrimaryType")
        val chips = createRefFor("chips")
        val storeImage = createRefFor("storeImage")

        constrain(storeTitle) {
            top.linkTo(parent.top)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storePrimaryType) {
            top.linkTo(storeTitle.bottom, 7.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(chips) {
            linkTo(top = storePrimaryType.bottom, bottom = parent.bottom, topMargin = 11.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storeImage) {
            end.linkTo(parent.end)
            linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
        }
    }
}

@Composable
fun StoreListDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DEFAULT_MARGIN.dp),
        thickness = 0.3.dp, color = SemiLightGray
    )
}