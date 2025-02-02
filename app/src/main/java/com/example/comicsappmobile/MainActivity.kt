package com.example.comicsappmobile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.navigation.AppNavHost
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme
import com.example.comicsappmobile.utils.Logger
import kotlinx.coroutines.launch
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

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(navController = navController, drawerState = drawerState)
                    },
                    scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                ) {
                    Scaffold(
                        topBar = { }
                    ) { innerPadding ->
                        AppNavHost(navController, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}


@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, globalState: GlobalState = koinInject()) {
    val authUser = globalState.authUser.collectAsState()
    val scope = rememberCoroutineScope()


    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Comics app mobile",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        // Buttons
        TextButton(
            onClick =
            {
                scope.launch {
                    navController.navigate(Screen.Catalog.route)
                    drawerState.close()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "",
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Каталог",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
        TextButton(
            onClick =
            {
                scope.launch {
                    if (authUser.value.userId > 0)
                        navController.navigate(Screen.ProfileUserScreen.route)
                    else
                        navController.navigate(Screen.LoginForm.route)
                    drawerState.close()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector =
                    if (authUser.value.userId > 0) Icons.Outlined.AccountCircle
                    else Icons.Outlined.Lock,
                    "",
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = if (authUser.value.userId > 0) "Профиль" else "Авторизироваться",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
        TextButton(
            onClick =
            {
                scope.launch {
                    navController.navigate(Screen.SettingsScreen.route)
                    drawerState.close()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    "",
                    modifier = Modifier.padding(end = 12.dp)
                )
                Text(
                    text = "Настройки",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
        // TODO: Remove from release
        if (BuildConfig.DEBUG) {
            HorizontalDivider(color = Color.Gray)
            Text(
                text = "Debug",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 12.dp)
            )

            TextButton(
                onClick =
                {
                    scope.launch {
                        navController.navigate(Screen.FontDisplayExamples.route)
                        drawerState.close()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        "",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Цветовая схема",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                }
            }

            TextButton(
                onClick =
                {
                    scope.launch {
                        navController.navigate(Screen.DragAndDropExample.route)
                        drawerState.close()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        "",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Drag & Drop",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                }
            }

            TextButton(
                onClick =
                {
                    scope.launch {
                        navController.navigate(Screen.EditedBookScreen.createRoute(-1))
                        drawerState.close()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, start = 12.dp, end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        "",
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "Добавить книгу",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                }
            }
        }
    }
}
