package com.example.comicsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// imports screens
import com.example.comicsapp.ui.view.screens.LeftNavMenuScreen
import com.example.comicsapp.ui.view.screens.PersonalAppBarScreen
import com.example.comicsapp.ui.viewmodel.AppViewModel

import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.memory.MemoryCache
import com.example.comicsapp.data.repository.BookRepository
import com.example.comicsapp.ui.view.components.ApiErrorDialog
import com.example.comicsapp.ui.navigation.Screen
import com.example.comicsapp.ui.view.theme.ComicsAppTheme
import com.example.comicsapp.ui.viewmodel.AppViewModelFactory
import com.example.comicsapp.ui.viewmodel.SettingsViewModel
import com.example.comicsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


// Глобальная декларация DataStore как расширения для Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var bookRepository: BookRepository

    private val appViewModel: AppViewModel by viewModels {
        AppViewModelFactory(
            context = applicationContext,
            owner = this
        )
    }

    lateinit var imageLoader: ImageLoader
        private set



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            imageLoader = ImageLoader.Builder(this.applicationContext)
                .memoryCache {
                    MemoryCache.Builder(this.applicationContext)
                        .maxSizePercent(0.1) // Максимальный размер кэша в памяти (25% от доступной памяти)
                        .build()
                }
                .build()

            val navController = rememberNavController()
            val drawerState = rememberDrawerState(DrawerValue.Closed) // Управление состоянием Drawer
            val scope = rememberCoroutineScope()

            val isDarkTheme = appViewModel.settings.isDarkTheme.collectAsState()


            // navConroller находиться в PersonalAppBarScreen
            ComicsAppTheme(isDarkTheme.value) {
                val errorApi = appViewModel.apiError.collectAsState()
                var showDialog by remember { mutableStateOf(false ) }

                if (errorApi.value != null && !errorApi.value!!.isErrorChecked())
                    showDialog = true


                ApiErrorDialog (
                    errorApi.value,
                    showDialog = showDialog,
                    onDismiss = {
                        appViewModel.setApiError( null )
                        showDialog = false
                    }
                )
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        LeftNavMenuScreen(navController, onCloseDrawer = {
                            scope.launch { drawerState.close() }
                        })
                    }
                ) {
                    PersonalAppBarScreen(navController, appViewModel, onMenuClick = {
                        scope.launch { drawerState.open() }
                    })

                }
            }
        }
    }
}







@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Профиль", style = MaterialTheme.typography.titleLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Настройки", style = MaterialTheme.typography.titleLarge)

        Column (
            modifier = Modifier
                .fillMaxWidth(),
        ){
            Text("Темная тема", style = MaterialTheme.typography.titleLarge)

            var expanded by remember { mutableStateOf(false) }
            val isDarkTheme = appViewModel.settings.isDarkTheme.collectAsState()

            var selectedOption by remember {
                mutableStateOf(
                    appViewModel.settings.getAppThemeName()
                )}
            // Список элементов


            // Поле ввода с выпадающим меню
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                // Текстовое поле
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Выбор темы приложения") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
                )

                // Само меню
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for ((key, value) in SettingsViewModel.appThemes){
                        DropdownMenuItem(
                            text = { Text(value) },
                            onClick = {
                                selectedOption = value
                                expanded = false
                                appViewModel.settings.setAppTheme(key)
                            }
                        )
                    }
                }
            }

        }

    }




}