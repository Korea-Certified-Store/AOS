package com.example.presentation.ui.map.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.presentation.R
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.summary.StoreOpeningTime
import com.example.presentation.ui.map.summary.StorePrimaryTypeText
import com.example.presentation.ui.map.summary.StoreTitle
import com.example.presentation.ui.map.summary.StoreTypeChips
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.util.MainConstants
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun StoreDetailInfo(
    storeInfo: StoreDetail
) {
    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            constraintSet = setDetailBottomSheetConstraints()
        ) {
            StoreTitle(storeInfo.displayName, "storeTitle")
            StorePrimaryTypeText(
                storeInfo.primaryTypeDisplayName ?: "상점", "storePrimaryType"
            )
            StoreTypeChips(storeInfo.certificationName, "chips")
            StoreDivider("divider")

            StoreClockImage("clockImage")
            StoreOpeningTime(
                storeInfo.operatingType,
                storeInfo.timeDescription,
                "storeOpeningTime"
            )
            StoreOpeningTimeOfWeek(
                storeInfo.operationTimeOfWeek,
                "storeOpeningTimeOfWeek"
            )

            StorePhoneImage("phoneImage")
            StorePhoneNumber(storeInfo.phoneNumber, "phoneNumberInfo")

            StoreAddressImage("addressImage")
            StoreAddressInfo(
                storeInfo.formattedAddress,
                "addressInfo",
                maxWidth
            )

            StoreDetailImageCard("storeImage", storeInfo.localPhotos)
        }
    }
}

@Composable
fun StoreOpeningTimeOfWeek(operationTimeOfWeek: Map<String, List<String>>, id: String) {
    Column(
        modifier = Modifier
            .layoutId(id)
            .wrapContentSize()
    ) {
        operationTimeOfWeek.forEach { operationTimeOfDay ->
            Row(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .wrapContentSize()
            ) {
                Text(
                    text = operationTimeOfDay.key,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .wrapContentSize()
                )
                Column {
                    operationTimeOfDay.value.forEach {
                        Text(
                            text = it,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(start = 8.dp, top = 1.dp)
                        )
                    }
                }
            }
        }
    }
}

fun setDetailBottomSheetConstraints(): ConstraintSet {
    return ConstraintSet {
        val storeTitle = createRefFor("storeTitle")
        val storePrimaryType = createRefFor("storePrimaryType")
        val chips = createRefFor("chips")
        val divider = createRefFor("divider")
        val clockImage = createRefFor("clockImage")
        val storeOpeningTime = createRefFor("storeOpeningTime")
        val storeOpeningTimeOfWeek = createRefFor("storeOpeningTimeOfWeek")

        val phoneImage = createRefFor("phoneImage")
        val phoneNumberInfo = createRefFor("phoneNumberInfo")

        val addressImage = createRefFor("addressImage")
        val addressInfo = createRefFor("addressInfo")

        val storeImage = createRefFor("storeImage")

        constrain(storeTitle) {
            top.linkTo(parent.top, 12.dp)
            linkTo(
                start = parent.start,
                startMargin = 16.dp,
                end = storeImage.start,
                endMargin = 8.dp,
                bias = 0F
            )
        }
        constrain(storePrimaryType) {
            top.linkTo(storeTitle.bottom, 5.dp)
            linkTo(
                start = parent.start,
                startMargin = 16.dp,
                end = storeImage.start,
                endMargin = 8.dp,
                bias = 0F
            )
        }
        constrain(chips) {
            top.linkTo(storePrimaryType.bottom, 5.dp)
            linkTo(
                start = parent.start,
                startMargin = 16.dp,
                end = storeImage.start,
                endMargin = 8.dp,
                bias = 0F
            )
        }
        constrain(divider) {
            top.linkTo(chips.bottom, 17.dp)
        }

        constrain(clockImage) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(divider.bottom, 17.dp)
        }

        constrain(storeOpeningTime) {
            start.linkTo(clockImage.end, 10.dp)
            top.linkTo(divider.bottom, 17.dp)
            width = Dimension.fillToConstraints
        }

        constrain(storeOpeningTimeOfWeek) {
            start.linkTo(clockImage.end, 10.dp)
            top.linkTo(storeOpeningTime.bottom, 8.dp)
            width = Dimension.fillToConstraints
        }

        constrain(phoneImage) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(storeOpeningTimeOfWeek.bottom, 14.dp)
        }
        constrain(phoneNumberInfo) {
            start.linkTo(phoneImage.end, 11.dp)
            top.linkTo(storeOpeningTimeOfWeek.bottom, 14.dp)
        }

        constrain(addressImage) {
            start.linkTo(parent.start, 16.dp)
            top.linkTo(phoneNumberInfo.bottom, 19.dp)
        }
        constrain(addressInfo) {
            start.linkTo(addressImage.end, 13.dp)
            top.linkTo(phoneNumberInfo.bottom, 19.dp)
            width = Dimension.fillToConstraints
        }

        constrain(storeImage) {
            end.linkTo(parent.end, 16.dp)
            top.linkTo(divider.bottom, 17.dp)
        }
    }
}


@Composable
fun StoreDivider(id: String) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .layoutId(id),
        thickness = 7.dp, color = LightGray
    )
}


@Composable
fun StoreClockImage(id: String) {
    Image(
        painter = painterResource(R.drawable.icon_clock),
        contentDescription = "Clock Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(height = 14.dp, width = 14.dp)
            .layoutId(id)
    )
}


@Composable
fun StorePhoneImage(id: String) {
    Image(
        painter = painterResource(R.drawable.icon_phone),
        contentDescription = "Phone Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(height = 14.dp, width = 13.dp)
            .layoutId(id)
    )
}


@Composable
fun StoreAddressImage(id: String) {
    Image(
        painter = painterResource(R.drawable.icon_address),
        contentDescription = "Address Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(height = 16.dp, width = 11.dp)
            .layoutId(id)
    )
}

@Composable
fun StorePhoneNumber(phoneNumber: String?, id: String) {
    val phoneNumberText = phoneNumber ?: "전화번호 정보 없음"
    Text(
        text = phoneNumberText,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .layoutId(id),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
}

@Composable
fun StoreAddressInfo(addressInfo: String, id: String, maxWidth: Dp) {
    Text(
        text = addressInfo,
        modifier = Modifier
            .wrapContentHeight()
            .width(maxWidth - MainConstants.BOTTOM_SHEET_STORE_DETAIL_IMG_SIZE.dp - (MainConstants.DEFAULT_MARGIN * 2).dp - 38.dp)
            .layoutId(id),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
}

@Composable
fun StoreDetailImageCard(id: String, localPhotos: List<String>) {
    Card(
        modifier = Modifier
            .size(MainConstants.BOTTOM_SHEET_STORE_DETAIL_IMG_SIZE.dp)
            .layoutId(id),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.3.dp, SemiLightGray)
    ) {
        StoreDetailImage(localPhotos = localPhotos)
    }
}

@Composable
fun StoreDetailImage(localPhotos: List<String>) {
    if (localPhotos.isNotEmpty()) {
        CoilImage(
            imageModel = localPhotos.first(),
            contentScale = ContentScale.Crop,
            placeHolder = painterResource(R.drawable.empty_store_img),
            error = painterResource(R.drawable.empty_store_img)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.empty_store_img),
            contentDescription = "Store Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}