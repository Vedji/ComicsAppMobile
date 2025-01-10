package com.example.comicsapp.ui.view.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.comicsapp.ProfileScreen
import com.example.comicsapp.R
import com.example.comicsapp.SettingsScreen
import com.example.comicsapp.ui.components.text.TextFieldAppBar
import com.example.comicsapp.ui.view.screens.aboutbook.BookPreviewEditScreen
import com.example.comicsapp.ui.view.screens.readbook.BookPreviewReadScreen
import com.example.comicsapp.ui.view.screens.aboutbook.BookPreviewScreen
import com.example.comicsapp.ui.view.screens.auth.LoginScreen
import com.example.comicsapp.ui.view.screens.bookcatalog.CatalogScreen
import com.example.comicsapp.ui.viewmodel.AppViewModel


// Отвечает за перемещение между экранами
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalAppBarScreen(navController: NavHostController, appViewModel: AppViewModel, onMenuClick: () -> Unit) {

    var isSearchEnabled by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {


                        val currentBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStackEntry?.destination?.route
                        when (currentRoute) {
                            "book_catalog" -> {
                                TextFieldAppBar(
                                    query = searchQuery,
                                    onQueryChange = { searchQuery = it },
                                    onClose = { isSearchEnabled = false }
                                )
                            }

                            "settings" -> {
                                Text("Настройки")
                            }

                            "book_preview" -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End, // Выровнять элементы по правому краю
                                    verticalAlignment = Alignment.CenterVertically // Опционально: выравнивание по вертикали
                                ) {
                                    IconButton(onClick = {
                                        navController.navigate("book_preview_edit")
                                    }) {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            painter = painterResource(id = R.drawable.edit_svgrepo_com), // Замените на ваш ресурс
                                            contentDescription = "Описание иконки" // Для accessibility
                                        )
                                    }
                                }
                            }
                            "book_read_screen" -> {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End, // Выровнять элементы по правому краю
                                    verticalAlignment = Alignment.CenterVertically // Опционально: выравнивание по вертикали
                                ){
                                    val chapter by appViewModel.selectedBook.chapter.collectAsState()
                                    val page by appViewModel.selectedBook.bookPage.collectAsState()


                                    val chapterNumber = chapter?.chapterNumber ?: 0
                                    val chapterLength = chapter?.chapterLength ?: 0
                                    val pageNumber = page?.pageNumber ?: 0

                                    Text("Глава $chapterNumber (${pageNumber + 1}/$chapterLength)")

                                    Icon(
                                        painter = painterResource(id = R.drawable.bookmark_add_svgrepo_com), // Замените на ваш ID ресурса
                                        contentDescription = "Описание иконки",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.numbered_list_v3), // Замените на ваш ID ресурса
                                        contentDescription = "Описание иконки",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }

                            else -> {
                                Text("Приложение")
                            }
                        }

                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp),
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "Menu",
                            tint = Color(0xFFC7CFA6)

                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            // Линия сразу под TopAppBar
            HorizontalDivider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )

            // NavHost
            NavHost(
                navController = navController,
                startDestination = "book_preview",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("book_catalog") { CatalogScreen(navController, appViewModel) }
                composable("profile") { ProfileScreen() }
                composable("settings") { SettingsScreen(navController, appViewModel) }
                composable("book_preview") { BookPreviewScreen(navController, appViewModel) }
                composable("book_preview_edit") { BookPreviewEditScreen(navController, appViewModel) }
                composable("book_read_screen") { BookPreviewReadScreen(navController, appViewModel) }
                composable("login_screen") { LoginScreen(navController, appViewModel) }

            }

        }
    }
}
