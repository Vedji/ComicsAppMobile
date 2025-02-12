package com.example.comicsappmobile.ui.screen.profiles.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.viewmodel.LoginFormViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormScreen (
    navController: NavHostController,
    loginFormViewModel: LoginFormViewModel = koinViewModel(),
    drawerState: DrawerState
) {
    val userLoginV2 = loginFormViewModel.userLogin.collectAsState()
    val checkAuth = loginFormViewModel.globalState.authUser.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val appContext = LocalContext.current.applicationContext

    val loginInput = rememberSaveable { mutableStateOf("") }
    val passwordInput = rememberSaveable { mutableStateOf("") }



    Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.background
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } },
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                "Menu",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    },
                    title = {  }
                )
            }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 36.dp)
        ) {
            Spacer(modifier = Modifier.height(180.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Авторизация",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        text =
                        when (userLoginV2.value) {
                            is UiState.Error -> {
                                if (userLoginV2.value.typeError == appContext.getString(R.string.AuthorizationError))
                                    "Логин или пароль введены неправильно"
                                else
                                    userLoginV2.value.message ?: "Ошибка приложения"
                            }

                            is UiState.Success -> {
                                "Успешная авторизация"
                            }

                            is UiState.Loading -> {
                                "Проверка авторизации"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            // fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Justify
                        )
                    )
                    ThemedInputField(
                        textFieldValue = loginInput,
                        placeholder = "Логин",
                        modifier = Modifier.height(36.dp)
                    )
                    ThemedInputField(
                        textFieldValue = passwordInput,
                        placeholder = "Пароль",
                        modifier = Modifier.height(36.dp)
                    )
                    Button(
                        onClick = {
                            if (loginInput.value.length < 3 || passwordInput.value.length < 3) {
                                loginFormViewModel.setUserAuthError(
                                    message = "Логин или пароль слишком короткие!",
                                    typeError = appContext.getString(R.string.AuthorizationError)
                                )
                                return@Button
                            }
                            coroutineScope.launch {
                                loginFormViewModel.loginFromUsername(
                                    username = loginInput.value,
                                    password = passwordInput.value
                                )
                                delay(500)
                                if (userLoginV2.value is UiState.Success) {
                                    navController.navigate(Screen.ProfileUserScreen.route)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Войти")
                    }
                    TextButton(
                        onClick = { navController.navigate(Screen.RegistrationFormScreen.route) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Зарегистрироваться")
                    }
                }
            }
        }
    }
}
