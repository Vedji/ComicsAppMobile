package com.example.comicsapp.ui.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: String) = "detail/$itemId"
    }
}