package com.example.comicsappmobile.navigation

import com.example.comicsappmobile.utils.Logger

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object AboutBook : Screen("aboutBook/{itemId}") {
        fun createRoute(itemId: String) = "aboutBook/$itemId"
    }
    data object Catalog: Screen("catalog")
    data object BookReader: Screen("bookchapter/{bookId}/{chapterId}") {
        fun createRoute(bookId: Int, chapterId: Int): String{
            return "bookchapter/$bookId/$chapterId"
        }
    }
    data object LoginForm: Screen("loginFormScreen")


    data object FontDisplayExamples: Screen("FontDisplayExamples")
    data object DragAndDropExample: Screen("DragAndDropExample")


    data object ProfileUserScreen: Screen("ProfileUserScreen")

    data object SettingsScreen: Screen("SettingScreen")
}