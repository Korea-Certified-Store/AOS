package com.example.presentation.model

data class ScreenCoordinate(
    val northWest: Coordinate,
    val southWest: Coordinate,
    val southEast: Coordinate,
    val northEast: Coordinate
)