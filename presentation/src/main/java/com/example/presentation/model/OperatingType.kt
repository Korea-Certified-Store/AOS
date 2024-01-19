package com.example.presentation.model

enum class OperatingType(val description: String) {
    OPERATING("영업 중"),
    CLOSED("영업 종료"),
    DAY_OFF("휴무일"),
    BREAK_TIME("브레이크 타임")
}