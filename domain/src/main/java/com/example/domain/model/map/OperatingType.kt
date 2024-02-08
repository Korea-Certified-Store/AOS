package com.example.domain.model.map

enum class OperatingType(val description: String) {
    OPERATING("운영 중"),
    CLOSED("운영 종료"),
    DAY_OFF("휴무일"),
    BREAK_TIME("브레이크 타임"),
    EMPTY_INFO("영업시간 정보 없음")
}