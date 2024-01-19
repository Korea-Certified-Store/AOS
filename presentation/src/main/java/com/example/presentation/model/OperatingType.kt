package com.example.presentation.model

enum class OperatingType(val subscribe: String) {
    OPERATING("영업 중"),
    CLOSED("금일 영업 마감"),
    BEFORE_OPEN("영업 전"),
    DAY_OFF("휴무일"),
    BREAK_TIME("브레이크 타임")
}