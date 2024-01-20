package com.example.presentation.ui.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.presentation.R
import com.example.presentation.model.Coordinate
import com.example.presentation.model.Day
import com.example.presentation.model.OpeningHours
import com.example.presentation.model.OperatingTime
import com.example.presentation.model.OperatingType
import com.example.presentation.model.StoreDetail
import com.example.presentation.model.StoreType
import com.example.presentation.model.TimeInfo
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.LightBlue
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.LightYellow
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.Pink
import com.example.presentation.ui.theme.Red
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_HEIGHT_OFF
import com.example.presentation.util.MainConstants.BOTTOM_SHEET_STORE_IMG_SIZE
import com.example.presentation.util.MainConstants.DEFAULT_MARIN
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreSummaryBottomSheet(
    heightType: Int,
    clickedStoreInfo: StoreDetail,
    onCallDialogChanged: (Boolean) -> Unit
) {
    BottomSheetScaffold(
        sheetContent = {
            Column {
                StoreSummaryInfo(clickedStoreInfo, onCallDialogChanged)
            }
        },
        sheetPeekHeight = heightType.dp,
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
                        .background(Color.LightGray)
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
    onCallDialogChanged: (Boolean) -> Unit
) {
    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = DEFAULT_MARIN.dp, vertical = 13.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            constraintSet = bottomSheetConstraints()
        ) {
            StoreTitle(
                storeInfo.displayName,
                storeInfo.primaryTypeDisplayName ?: "상점",
                "storeTitle",
                maxWidth
            )
            Chips(storeInfo.certificationName, "chips")
            StoreOpeningTime(storeInfo.regularOpeningHours, "storeOpeningTime")
            StoreCallButton(onCallDialogChanged, "storeCallButton")
            StoreImage("storeImage")
        }
    }
}

fun bottomSheetConstraints(): ConstraintSet {
    return ConstraintSet {
        val storeTitle = createRefFor("storeTitle")
        val chips = createRefFor("chips")
        val storeOpeningTime = createRefFor("storeOpeningTime")
        val storeCallButton = createRefFor("storeCallButton")
        val storeImage = createRefFor("storeImage")

        constrain(storeTitle) {
            top.linkTo(parent.top)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 100.dp, bias = 0F)
        }
        constrain(chips) {
            start.linkTo(parent.start)
            top.linkTo(storeTitle.bottom, 4.dp)
        }
        constrain(storeOpeningTime) {
            start.linkTo(parent.start)
            top.linkTo(chips.bottom, 3.dp)
        }
        constrain(storeCallButton) {
            start.linkTo(parent.start)
            top.linkTo(storeOpeningTime.bottom, 5.dp)
            bottom.linkTo(parent.bottom)
        }
        constrain(storeImage) {
            end.linkTo(parent.end)
            top.linkTo(parent.top)
        }
    }
}

@Composable
fun StoreTitle(storeName: String, storeType: String, id: String, maxWidth: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .layoutId(id)
            .width(maxWidth - BOTTOM_SHEET_STORE_IMG_SIZE.dp - (DEFAULT_MARIN * 2).dp)
    ) {
        Text(
            text = storeName,
            color = MediumBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.layoutId(id),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = storeType,
            color = MediumGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun Chip(
    storeType: StoreType
) {
    Surface(
        color = when (storeType) {
            StoreType.KIND -> Pink
            StoreType.GREAT -> LightYellow
            StoreType.SAFE -> LightBlue
        },
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(
            text = stringResource(storeType.storeTypeName),
            color = MediumGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Thin,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun Chips(
    elements: List<StoreType>,
    id: String
) {
    LazyRow(modifier = Modifier.layoutId(id)) {
        items(elements.size) { idx ->
            Chip(storeType = elements[idx])
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }

}

@Composable
fun StoreOpeningTime(
    regularOpeningHours: List<OpeningHours>,
    id: String
) {
    val operatingTime = getOperatingType(regularOpeningHours)

    Row(modifier = Modifier.layoutId(id)) {
        Text(
            text = stringResource(operatingTime.operatingType.description),
            Modifier.alignByBaseline(),
            color = Red,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = operatingTime.timeDescription,
            Modifier.alignByBaseline(),
            color = MediumGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

fun getOperatingType(regularOpeningHours: List<OpeningHours>): OperatingTime {
    val nowTime = getNowTimeInfo()
    val operatingTimes = regularOpeningHours.filter { it.open.day == nowTime.day }
    val nextDay =
        regularOpeningHours.filter { it.open.day == Day.values()[(nowTime.day.ordinal + 1) % 7] }

    val type = if (operatingTimes.isEmpty()) {
        OperatingTime(OperatingType.DAY_OFF, "")
    } else if (operatingTimes.size == 1 && (operatingTimes.first().open.hour == 0 && operatingTimes.first().open.minute == 0) && (operatingTimes.first().close.hour == 0 && operatingTimes.first().close.minute == 0)) {
        OperatingTime(OperatingType.OPERATING, "24시간 영업")
    } else if (nowTime.hour * 60 + nowTime.minute < operatingTimes.first().open.hour * 60 + operatingTimes.first().open.minute) {
        OperatingTime(
            OperatingType.CLOSED,
            "${operatingTimes.first().open.hour}:${getOperatingMinute(operatingTimes.first().open.minute)}에 영업 시작"
        )
    } else if (nowTime.hour * 60 + nowTime.minute > getCloseHour(operatingTimes.last().close.hour) * 60 + operatingTimes.last().close.minute) {
        OperatingTime(OperatingType.CLOSED, getNextDayOpeningHour(nextDay))
    } else {
        calculateOperating(nowTime, operatingTimes, nextDay)
    }

    return type
}

fun getNextDayOpeningHour(nextDay: List<OpeningHours>): String {
    return if (nextDay.isEmpty()) "" else "${nextDay.first().open.hour}:${getOperatingMinute(nextDay.first().open.minute)}에 영업 시작"
}

fun getOperatingMinute(minute: Int): String {
    return if (minute == 0) "00" else minute.toString()
}

fun getNowTimeInfo(): TimeInfo {
    val currentDate = Date()
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = currentDate

    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val day = Day.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1]

    return TimeInfo(day, hour, minute)
}

fun calculateOperating(
    nowTime: TimeInfo,
    operatingTimes: List<OpeningHours>,
    nextDay: List<OpeningHours>
): OperatingTime {
    for (idx in 0 until operatingTimes.size - 1) {
        if (abs(operatingTimes[idx].close.hour * 60 + operatingTimes[idx].close.minute - operatingTimes[idx + 1].open.hour * 60 + operatingTimes[idx + 1].open.minute) <= 3 * 60) {
            if (nowTime.hour * 60 + nowTime.minute in (operatingTimes[idx].close.hour * 60 + operatingTimes[idx].close.minute until operatingTimes[idx + 1].open.hour * 60 + operatingTimes[idx + 1].open.minute)) {
                return OperatingTime(
                    OperatingType.BREAK_TIME,
                    "${operatingTimes[idx + 1].open.hour}:${getOperatingMinute(operatingTimes[idx + 1].open.minute)}에 영업 시작"
                )
            }
        } else if (nowTime.hour * 60 + nowTime.minute in (operatingTimes[idx].open.hour * 60 + operatingTimes[idx].open.minute..getCloseHour(
                operatingTimes[idx].close.hour
            ) * 60 + operatingTimes[idx].close.minute)
        ) {
            return OperatingTime(
                OperatingType.OPERATING,
                "${operatingTimes[idx].open.hour}:${getOperatingMinute(operatingTimes[idx].open.minute)}에 브레이크 타임 시작"
            )
        }
    }

    if (nowTime.hour * 60 + nowTime.minute in (operatingTimes.last().open.hour * 60 + operatingTimes.last().open.minute..getCloseHour(
            operatingTimes.last().close.hour
        ) * 60 + operatingTimes.last().close.minute)
    ) {
        return OperatingTime(
            OperatingType.OPERATING,
            "${operatingTimes.last().close.hour}:${getOperatingMinute(operatingTimes.last().close.minute)}에 영업 종료"
        )
    } else if (nowTime.hour * 60 + nowTime.minute < operatingTimes.last().open.hour * 60 + operatingTimes.last().open.minute) {
        return OperatingTime(
            OperatingType.CLOSED,
            "${operatingTimes.last().open.hour}:${getOperatingMinute(operatingTimes.last().open.minute)}에 영업 시작"
        )
    }

    return OperatingTime(OperatingType.CLOSED, getNextDayOpeningHour(nextDay))
}

fun getCloseHour(hour: Int) = if (hour == 0) 24 else hour

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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 1.dp),
            colors = ButtonDefaults.buttonColors(containerColor = White),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.call),
                tint = DarkGray,
                contentDescription = "Call",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StoreImage(id: String) {
    Card(
        modifier = Modifier
            .size(BOTTOM_SHEET_STORE_IMG_SIZE.dp)
            .layoutId(id),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.3.dp, LightGray)
    ) {
        Image(
            painter = painterResource(R.drawable.store_example),
            contentDescription = "Store Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}