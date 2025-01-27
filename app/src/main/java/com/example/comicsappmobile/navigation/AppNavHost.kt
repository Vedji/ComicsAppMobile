package com.example.comicsappmobile.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.comicsappmobile.ui.screen.profiles.auth.LoginFormScreen
import com.example.comicsappmobile.ui.screen.catalog.CatalogScreen
import com.example.comicsappmobile.ui.screen.book.BookScreen
import com.example.comicsappmobile.ui.screen.bookeditor.BookEditorScreen
import com.example.comicsappmobile.ui.screen.bookreader.BookReaderScreen
import com.example.comicsappmobile.ui.screen.chaptereditor.ChapterEditorScreen
import com.example.comicsappmobile.ui.screen.examples.DragAndDropExample
import com.example.comicsappmobile.ui.screen.examples.FontDisplayExamplesScreen
import com.example.comicsappmobile.ui.screen.profiles.ProfileScreen
import com.example.comicsappmobile.ui.screen.settings.SettingScreen
import com.example.comicsappmobile.utils.Logger

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthDp = configuration.screenWidthDp
    val screenWidthPx = with(density) { screenWidthDp.dp.toPx().toInt() }

    NavHost(
        navController = navController,
        startDestination = Screen.Catalog.route,
        modifier = modifier.fillMaxSize()
    ) {
        // Main screen
        composable(route = Screen.Main.route) {
            MainScreen(onItemClick = { itemId ->
                navController.navigate(Screen.AboutBook.createRoute(itemId))
            })
        }

        // About book screen
        composable(
            route = Screen.AboutBook.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType },
                navArgument("selectionTab") { type = NavType.IntType },
            ),
            // enterTransition = {  ->
            //     slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn()
            // },
            // exitTransition = {  ->
            //     slideOutHorizontally(targetOffsetX = { -screenWidthPx }) + fadeOut()
            // },
            // popEnterTransition = { ->
            //     slideInHorizontally(initialOffsetX = { -screenWidthPx }) + fadeIn()
            // },
            // popExitTransition = {  ->
            //     slideOutHorizontally(targetOffsetX = { screenWidthPx }) + fadeOut()
            // }
        ) { backStackEntry ->
            val itemId: String = backStackEntry.arguments?.getString ("itemId") ?: ""
            val intItemId = itemId.toIntOrNull() ?: -1
            val selectionTab = backStackEntry.arguments?.getInt ("selectionTab") ?: 0
            BookScreen(navController = navController, bookId = intItemId, selectionTab = selectionTab)
        }

        // Catalog Screen
        composable(
            route = Screen.Catalog.route,
        ) {
            CatalogScreen(navController)
        }

        composable(
            route = Screen.LoginForm.route,
        ) {
            LoginFormScreen(navController)
        }
        // Examples start
        composable(route = Screen.FontDisplayExamples.route,) { FontDisplayExamplesScreen() }
        composable(route = Screen.DragAndDropExample.route){ DragAndDropExample() }
        // Examples end

        composable(route = Screen.SettingsScreen.route){ SettingScreen(navController) }

        composable(
            route = Screen.BookReader.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("chapterId") { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: -1
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: -1
            Logger.debug("NavHost -> composable", "screen = '${Screen.BookReader.route}', bookId = '$bookId', chapterId = '$chapterId', backStackEntry = '${backStackEntry.destination.route}'")

            BookReaderScreen(
                bookId = bookId,
                chapterId = chapterId,
                navController = navController
            )
        }

        composable(route = Screen.ProfileUserScreen.route){
            ProfileScreen(navController = navController)
        }

        composable(
            route = Screen.EditedBookScreen.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val bookId: Int = backStackEntry.arguments?.getInt ("bookId") ?: -1
            BookEditorScreen(bookId = bookId, navController = navController)
        }

        composable(
            route = Screen.ChapterEditorScreen.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.IntType },
                navArgument("chapterId") { type = NavType.IntType }
            ),
        ) { backStackEntry ->
            val bookId: Int = backStackEntry.arguments?.getInt ("bookId") ?: -1
            val chapterId: Int = backStackEntry.arguments?.getInt ("chapterId") ?: -1
            ChapterEditorScreen(bookId = bookId, chapterId = chapterId, navController = navController)
        }

    }
}


@Composable
fun MainScreen(onItemClick: (String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Item 1",
            modifier = Modifier.clickable { onItemClick("1") }
        )
        Text(
            text = "Item 2",
            modifier = Modifier.clickable { onItemClick("2") }
        )
        Text(
            text = "Item 3",
            modifier = Modifier.clickable { onItemClick("3") }
        )
    }
}

@Composable
fun DetailScreen(itemId: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Detail Screen")
        Text(text = "Item ID: $itemId")
    }
}