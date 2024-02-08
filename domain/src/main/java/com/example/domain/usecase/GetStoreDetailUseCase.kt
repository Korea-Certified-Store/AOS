package com.example.domain.usecase

import com.example.domain.model.map.Day
import com.example.domain.model.map.OpeningHoursModel
import com.example.domain.model.map.OperatingTime
import com.example.domain.model.map.OperatingType
import com.example.domain.model.map.StoreDetail
import com.example.domain.model.map.StoreDetailModel
import com.example.domain.model.map.TimeInfoModel
import com.example.domain.repository.StoreDetailRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.Calendar
import java.util.Date
import kotlin.math.abs

class GetStoreDetailUseCase(
    private val repository: StoreDetailRepository
) {
    suspend operator fun invoke(
        nwLong: Double,
        nwLat: Double,
        swLong: Double,
        swLat: Double,
        seLong: Double,
        seLat: Double,
        neLong: Double,
        neLat: Double,
    ): Flow<Resource<List<List<StoreDetail>>>> = flow {
        emit(Resource.Loading())
        repository.getStoreDetail(
            nwLong,
            nwLat,
            swLong,
            swLat,
            seLong,
            seLat,
            neLong,
            neLat
        ).fold(onSuccess = { items ->
            emit(Resource.Success(items.map {
                it.map { storeDetailModel ->
                    val operatingType = getOperatingType(storeDetailModel.regularOpeningHours)
                    val operationTimeOfWeek = getOperationTimeOfWeek(storeDetailModel)
                    StoreDetail(
                        id = storeDetailModel.id,
                        displayName = storeDetailModel.displayName,
                        primaryTypeDisplayName = storeDetailModel.primaryTypeDisplayName,
                        formattedAddress = storeDetailModel.formattedAddress,
                        phoneNumber = storeDetailModel.phoneNumber,
                        location = storeDetailModel.location,
                        operatingType = operatingType.operatingType.description,
                        timeDescription = operatingType.timeDescription,
                        localPhotos = storeDetailModel.localPhotos,
                        certificationName = storeDetailModel.certificationName,
                        operationTimeOfWeek = operationTimeOfWeek
                    )
                }
            }))
        }, onFailure = { e ->
            if (e is IOException) {
                emit(Resource.Failure("서버와의 통신이 원활하지 않습니다."))
            } else {
                emit(Resource.Failure("데이터를 불러올 수 없습니다."))
            }
        })
    }

    private fun getOperationTimeOfWeek(storeDetailModel: StoreDetailModel): Map<String, List<String>> {
        return if (storeDetailModel.regularOpeningHours.isNotEmpty()) {
            getOperatingTimeOfWeekString(storeDetailModel.regularOpeningHours)
        } else emptyMap()
    }

    private fun getOperatingType(regularOpeningHours: List<OpeningHoursModel>): OperatingTime {
        val nowTime = getNowTimeInfo()
        val operatingTimes = regularOpeningHours.filter { it.open.day == nowTime.day }
        val nextDay =
            regularOpeningHours.filter { getDay(it.open.day) == Day.values()[(getDay(nowTime.day).ordinal + 1) % 7] }

        val type = if (regularOpeningHours.isEmpty()) {
            OperatingTime(OperatingType.EMPTY_INFO, "")
        } else if (operatingTimes.isEmpty()) {
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

    private fun getDay(day: String): Day {
        for (value in Day.values()) {
            if (day == value.name) {
                return value
            }
        }
        return Day.MON
    }

    private fun getNextDayOpeningHour(nextDay: List<OpeningHoursModel>): String {
        return if (nextDay.isEmpty()) "" else "${nextDay.first().open.hour}:${
            getOperatingMinute(
                nextDay.first().open.minute
            )
        }에 영업 시작"
    }

    private fun getOperatingMinute(minute: Int): String {
        return if (minute == 0) "00" else minute.toString()
    }

    private fun getNowTimeInfo(): TimeInfoModel {
        val currentDate = Date()
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = currentDate

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val day = Day.values()[calendar.get(Calendar.DAY_OF_WEEK) - 1].name

        return TimeInfoModel(day, hour, minute)
    }

    private fun calculateOperating(
        nowTime: TimeInfoModel,
        operatingTimes: List<OpeningHoursModel>,
        nextDay: List<OpeningHoursModel>
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
                "${getCloseHour(operatingTimes.last().close.hour)}:${
                    getOperatingMinute(
                        operatingTimes.last().close.minute
                    )
                }에 영업 종료"
            )
        } else if (nowTime.hour * 60 + nowTime.minute < operatingTimes.last().open.hour * 60 + operatingTimes.last().open.minute) {
            return OperatingTime(
                OperatingType.CLOSED,
                "${operatingTimes.last().open.hour}:${getOperatingMinute(operatingTimes.last().open.minute)}에 영업 시작"
            )
        }

        return OperatingTime(OperatingType.CLOSED, getNextDayOpeningHour(nextDay))
    }

    private fun getCloseHour(hour: Int) = if (hour == 0) 24 else hour

    private fun getOperatingTimeOfWeekString(data: List<OpeningHoursModel>): Map<String, List<String>> {
        val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
        val daysKorean = listOf("월", "화", "수", "목", "금", "토", "일")
        val result = mutableMapOf<String, List<String>>()

        for ((index, day) in days.withIndex()) {
            val dayData = data.filter { it.open.day == day }

            result[daysKorean[index]] = when {
                dayData.isEmpty() -> listOf("휴무일")
                dayData.size == 1 -> getOperatingTime(dayData)
                else -> getOperatingAndBreakTime(dayData)
            }
        }
        return result
    }

    private fun getOperatingTime(dayData: List<OpeningHoursModel>) =
        if (dayData[0].open.hour == 0 && dayData[0].open.minute == 0 && dayData[0].close.hour == 0 && dayData[0].close.minute == 0) {
            listOf("24시간 영업")
        } else {
            val openTime = formatTime(dayData[0].open.hour, dayData[0].open.minute)
            val closeTime =
                formatTime(getCloseHour(dayData[0].close.hour), dayData[0].close.minute)
            listOf("$openTime - $closeTime")
        }

    private fun getOperatingAndBreakTime(dayData: List<OpeningHoursModel>): List<String> {
        val openTime1 = formatTime(dayData[0].open.hour, dayData[0].open.minute)
        val closeTime1 =
            formatTime(getCloseHour(dayData[0].close.hour), dayData[0].close.minute)
        val openTime2 = formatTime(dayData[1].open.hour, dayData[1].open.minute)
        val closeTime2 =
            formatTime(getCloseHour(dayData[1].close.hour), dayData[1].close.minute)
        return listOf("$openTime1 - $closeTime2", "$closeTime1 - $openTime2 브레이크타임")
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }
}