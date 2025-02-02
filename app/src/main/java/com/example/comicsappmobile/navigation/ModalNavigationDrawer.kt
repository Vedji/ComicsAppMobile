package com.example.comicsappmobile.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.utils.vibrate
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelNavigationDrawer(navController: NavHostController, drawerState: DrawerState, globalState: GlobalState = koinInject()) {
    val authUser = globalState.authUser.collectAsState()
    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    val devModeSecret = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.background
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { coroutineScope.launch { drawerState.close() } },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            "Menu",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Comics app mobile",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            devModeSecret.value = !devModeSecret.value
                                            coroutineScope.launch { vibrate(context = context) }
                                        }
                                    )
                                }
                                .padding(end = 12.dp),
                        )
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.Start
        ) {
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
            AnimatedVisibility(
                visible = devModeSecret.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 350)) + slideInHorizontally(
                    animationSpec = tween(durationMillis = 350),
                    initialOffsetX = { -it }
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 350)) + slideOutHorizontally(
                    animationSpec = tween(durationMillis = 350),
                    targetOffsetX = { -it }
                )
            ) {
                Column {
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
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
    }
}