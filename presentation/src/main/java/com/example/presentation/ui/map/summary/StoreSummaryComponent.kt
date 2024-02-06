package com.example.presentation.ui.map.summary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.presentation.R
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.component.StoreImageCard
import com.example.presentation.ui.component.StorePrimaryTypeText
import com.example.presentation.ui.component.StoreTitleText
import com.example.presentation.ui.component.StoreTypeChips
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_STORE_IMG_SIZE
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN

@Composable
fun StoreSummaryInfo(
    storeInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit,
    onCurrentSummaryInfoHeightChanged: (Dp) -> Unit,
    currentSummaryInfoHeight: Dp
) {
    val density = LocalDensity.current

    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = DEFAULT_MARGIN.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .onSizeChanged { size ->
                    val newHeight = with(density) { size.height.toDp() }

                    if (newHeight != currentSummaryInfoHeight) {
                        onCurrentSummaryInfoHeightChanged(newHeight)
                    }
                },
            constraintSet = setBottomSheetConstraints()
        ) {
            StoreTitleText(storeInfo.displayName, 20, "storeTitle")
            StorePrimaryTypeText(storeInfo.primaryTypeDisplayName ?: "상점", 11, "storePrimaryType")
            StoreTypeChips(storeInfo.certificationName, "chips")
            StoreOpeningTime(storeInfo.operatingType, storeInfo.timeDescription, "storeOpeningTime")
            if (storeInfo.phoneNumber != null) StoreCallButton(
                onCallDialogChanged,
                "storeCallButton"
            )
            StoreImageCard(storeInfo.localPhotos, BOTTOM_SHEET_STORE_IMG_SIZE, "storeImage")
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
            top.linkTo(parent.top, 12.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storePrimaryType) {
            top.linkTo(storeTitle.bottom, 5.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(chips) {
            top.linkTo(storePrimaryType.bottom, 5.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storeOpeningTime) {
            top.linkTo(chips.bottom, 11.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storeCallButton) {
            start.linkTo(parent.start)
            linkTo(
                top = storeOpeningTime.bottom,
                bottom = parent.bottom,
                topMargin = 20.dp,
                bottomMargin = 13.dp,
                bias = 0F
            )
        }
        constrain(storeImage) {
            end.linkTo(parent.end)
            linkTo(
                top = parent.top,
                bottom = parent.bottom,
                topMargin = 12.dp,
                bottomMargin = 13.dp,
                bias = 0F
            )
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
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = White),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.call),
                tint = DarkGray,
                contentDescription = "Call",
                modifier = Modifier.size(21.dp)
            )
        }
    }
}