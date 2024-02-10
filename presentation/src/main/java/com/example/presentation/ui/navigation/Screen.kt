package com.example.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen(route = "main_screen")
    object Search : Screen(route = "search_screen")
}