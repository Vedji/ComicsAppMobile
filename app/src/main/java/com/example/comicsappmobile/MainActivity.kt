package com.example.comicsappmobile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.navigation.AppNavHost
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme
import com.example.comicsappmobile.utils.Logger
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val globalState: GlobalState = koinInject()
            val themeId by globalState.getAppThemeId().collectAsState(0)

            if (!globalState.firstUserAuth){
                LaunchedEffect(Unit) {
                    globalState.getUserAccessToken().collect {
                        if (it == null || globalState.firstUserAuth)
                            return@collect  // TODO Replace to fetch
                        RetrofitInstance.accessToken = it
                        Logger.debug("LoginFormViewModel -> onCreate", "globalState.getUserAccessToken().collect = '$it'")
                        val repo = UserRepository(RetrofitInstance.userApi, sharedViewModel = SharedViewModel(), globalState = globalState)
                        repo.responseRefreshUserAuthorization(it)
                        globalState.firstUserAuth = true
                    }.toString()
                }

            }

            ComicsAppMobileTheme(themeId) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(navController = navController)
                    }
                ) {
                    Scaffold(
                        topBar = {
                            /*
                            TopAppBar(
                                title = { Text("MyApp") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            )

                             */
                        }
                    ) { innerPadding ->
                        AppNavHost(navController, Modifier.padding(innerPadding))
                    }
                }

                // if (globalState.isOpenBottomSheet)
                //     ModalBottomSheet(
                //         onDismissRequest = { globalState.isOpenBottomSheet = false },
                //         sheetState = SheetState(
                //             skipPartiallyExpanded = globalState.bottomSheetSkipPartiallyExpanded,
                //             Density(LocalContext.current.applicationContext)
                //         )
                //     ){
                //         Text(text =  "Test")
                //     }
            }
        }
    }
}


@Composable
fun DrawerContent(navController: NavHostController) {
    Column {
        Text("Navigation Drawer", style = MaterialTheme.typography.titleLarge)
        HorizontalDivider(color = Color.Gray)
        // Example items
        TextButton(onClick = { navController.navigate(Screen.Catalog.route) }) {
            Text("Go to catalog")
        }
        TextButton(onClick = { navController.navigate(Screen.LoginForm.route) }) {
            Text("Go to login")
        }
        TextButton(onClick = { navController.navigate(Screen.FontDisplayExamples.route) }) {
            Text("Go to FontDisplayExamples") // TODO: Remove from release
        }
        TextButton(onClick = { navController.navigate(Screen.DragAndDropExample.route) }) {
            Text("Go to DragAndDropExample examples") // TODO: Remove from release
        }

        TextButton(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
            Text("Settings")

        }
    }
}





