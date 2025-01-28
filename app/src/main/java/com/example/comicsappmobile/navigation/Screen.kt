package com.example.comicsappmobile.navigation

import com.example.comicsappmobile.utils.Logger

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object AboutBook : Screen("aboutBook/{itemId}/{selectionTab}") {
        fun createRoute(itemId: String, selectionTab: Int = 0) = "aboutBook/$itemId/$selectionTab"
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
    data object EditedBookScreen : Screen("EditedBookScreen/{bookId}") {
        fun createRoute(bookId: Int) = "EditedBookScreen/$bookId"
    }
    data object ChapterEditorScreen : Screen("ChapterEditorScreen/{bookId}/{chapterId}") {
        fun createRoute(bookId: Int, chapterId: Int) = "ChapterEditorScreen/$bookId/$chapterId"
    }
    data object ProfileEditorScreen: Screen("ProfileEditorScreen")

}