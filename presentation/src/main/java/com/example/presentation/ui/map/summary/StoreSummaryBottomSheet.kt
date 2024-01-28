package com.example.presentation.ui.map.summary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
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
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_DEFAULT_PADDING
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_STORE_IMG_SIZE
import com.example.presentation.util.MainConstants.DEFAULT_MARIN
import com.skydoves.landscapist.coil.CoilImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    isMarkerClicked: Boolean,
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit,
    bottomSheetHeight: Dp,
    onHeightChanged: (Dp) -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(
                    clickedStoreInfo,
                    onCallDialogChanged,
                    onHeightChanged,
                    bottomSheetHeight
                )
            }
        },
        sheetPeekHeight = if (isMarkerClicked) bottomSheetHeight + BOTTOM_SHEET_DEFAULT_PADDING.dp else BOTTOM_SHEET_HEIGHT_OFF.dp,
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
                        .background(White)
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
    onCallDialogChanged: (Boolean) -> Unit,
    onHeightChanged: (Dp) -> Unit,
    currentHeight: Dp
) {
    val density = LocalDensity.current
    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = DEFAULT_MARIN.dp, vertical = 12.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .onSizeChanged { size ->
                    val newHeight = with(density) { size.height.toDp() }

                    if (newHeight != currentHeight) {
                        onHeightChanged(newHeight)
                    }
                },
            constraintSet = setBottomSheetConstraints()
        ) {
            StoreTitle(
                storeInfo.displayName,
                "storeTitle",
                maxWidth
            )
            StorePrimaryTypeText(
                storeInfo.primaryTypeDisplayName ?: "상점",
                "storePrimaryType",
                maxWidth
            )
            StoreTypeChips(storeInfo.certificationName, "chips", maxWidth)
            StoreOpeningTime(storeInfo.operatingType, storeInfo.timeDescription, "storeOpeningTime")
            if (storeInfo.phoneNumber != null) StoreCallButton(
                onCallDialogChanged,
                "storeCallButton"
            )
            StoreImageCard("storeImage", storeInfo.localPhotos)
        }
    }
}

fun setBottomSheetConstraints(): ConstraintSet {
    return ConstraintSet {
        val storeTitle = createRefFor("storeTitle")
        val storePrimaryType = createRefFor("storePrimaryType")
        val chips = createRefFor("chips")
        val storeOpeningTime = createRefFor("storeOpeningTime")
        val storeCallButton = createRefFor("storeCallButton")
        val storeImage = createRefFor("storeImage")

        constrain(storeTitle) {
            top.linkTo(parent.top)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
        }
        constrain(storePrimaryType) {
            top.linkTo(storeTitle.bottom, 5.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
        }
        constrain(chips) {
            top.linkTo(storePrimaryType.bottom, 5.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
        }
        constrain(storeOpeningTime) {
            start.linkTo(parent.start)
            top.linkTo(chips.bottom, 11.dp)
        }
        constrain(storeCallButton) {
            start.linkTo(parent.start)
            top.linkTo(storeOpeningTime.bottom, 21.dp)
            bottom.linkTo(parent.bottom)
        }
        constrain(storeImage) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }
    }
}

@Composable
fun StoreTitle(storeName: String, id: String, maxWidth: Dp) {
    Text(
        text = storeName,
        color = MediumBlue,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .layoutId(id)
            .width(maxWidth - BOTTOM_SHEET_STORE_IMG_SIZE.dp - (DEFAULT_MARIN * 2).dp),
    )
}

@Composable
fun StorePrimaryTypeText(storeType: String, id: String, maxWidth: Dp) {
    Text(
        text = storeType,
        color = MediumGray,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .layoutId(id)
            .width(maxWidth - BOTTOM_SHEET_STORE_IMG_SIZE.dp - (DEFAULT_MARIN * 2).dp),
    )
}

@Composable
fun StoreTypeChip(
    storeType: StoreType
) {
    Surface(
        color = when (storeType) {
            StoreType.KIND -> Pink
            StoreType.GREAT -> LightYellow
            StoreType.SAFE -> LightBlue
        },
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Text(
            text = stringResource(storeType.storeTypeName),
            color = MediumGray,
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StoreTypeChips(
    elements: List<StoreType>,
    id: String,
    maxWidth: Dp
) {
    FlowRow(
        modifier = Modifier
            .layoutId(id)
            .width(maxWidth - BOTTOM_SHEET_STORE_IMG_SIZE.dp - (DEFAULT_MARIN * 2).dp),
    ) {
        elements.forEach { item ->
            StoreTypeChip(storeType = item)
        }
    }
}

@Composable
fun StoreOpeningTime(
    operatingType: String,
    timeDescription: String,
    id: String
) {
    Row(
        modifier = Modifier.layoutId(id),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = operatingType,
            Modifier.alignByBaseline(),
            color = Red,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.width(4.dp))
        StoreOpeningTimeCircle()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = timeDescription,
            Modifier.alignByBaseline(),
            color = MediumGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun StoreOpeningTimeCircle() {
    Box(
        modifier = Modifier
            .size(2.dp)
            .background(color = LightGray, shape = CircleShape),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreCallButton(
    onCallDialogChanged: (Boolean) -> Unit,
    id: String
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            onClick = {
                onCallDialogChanged(true)
            },
            modifier = Modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                .layoutId(id),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = White),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.call),
                tint = DarkGray,
                contentDescription = "Call",
                modifier = Modifier.size(29.dp)
            )
        }
    }
}

@Composable
fun StoreImageCard(id: String, localPhotos: List<String>) {
    Card(
        modifier = Modifier
            .size(BOTTOM_SHEET_STORE_IMG_SIZE.dp)
            .layoutId(id),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.3.dp, SemiLightGray)
    ) {
        StoreImage(localPhotos = localPhotos)
    }
}

@Composable
fun StoreImage(localPhotos: List<String>) {
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