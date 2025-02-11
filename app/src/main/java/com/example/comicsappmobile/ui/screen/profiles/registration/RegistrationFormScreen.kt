package com.example.comicsappmobile.ui.screen.profiles.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Lock
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.data.mapper.UserMapper
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.StarVisualTransformation
import com.example.comicsappmobile.ui.components.ThemedInputField
import com.example.comicsappmobile.ui.presentation.viewmodel.RegistrationFormViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationFormScreen (
    navController: NavHostController,
    registrationFormViewModel: RegistrationFormViewModel = koinViewModel(),
    drawerState: DrawerState
) {
    val authUserState by registrationFormViewModel.userLogin.collectAsState()
    val authUserValue by registrationFormViewModel.globalState.authUser.collectAsState()

    LaunchedEffect(authUserValue) {
        if (authUserValue.userId <= 0) {
            registrationFormViewModel.setUserState(UiState.Error(message = ""))
            return@LaunchedEffect
        }
        registrationFormViewModel.setUserState(UiState.Success(data = UserMapper.map(authUserValue)))
    }

    val coroutineScope = rememberCoroutineScope()
    val usernameInput = rememberSaveable { mutableStateOf("") }
    val mailInput = rememberSaveable { mutableStateOf("") }
    val passwordInput = rememberSaveable { mutableStateOf("") }
    val retryPasswordInput = rememberSaveable { mutableStateOf("") }



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
                .offset(y = (-128).dp)
        ) {
            Spacer(
                modifier = Modifier
                    .height(180.dp)
            )
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
                        text = "Регистрация",
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Light
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = when (authUserState) {
                            is UiState.Error -> {
                                if (authUserState.typeError == "ResourceExistsError")
                                    "Пользователь с таким именем или почтой уже существует"
                                else
                                    authUserState.message ?: "Неизвестная ошибка" }
                            is UiState.Loading -> { "" }
                            is UiState.Success -> { "Успешная авторизация" }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            textAlign = TextAlign.Justify
                        )
                    )
                    ThemedInputField(
                        textFieldValue = usernameInput,
                        placeholder = "Имя пользователя",
                        modifier = Modifier.height(36.dp)
                    )
                    ThemedInputField(
                        textFieldValue = mailInput,
                        placeholder = "Почта",
                        modifier = Modifier.height(36.dp)
                    )
                    var isPasswordVisibleFirst by remember { mutableStateOf(false) }
                    ThemedInputField(
                        textFieldValue = passwordInput,
                        placeholder = "Пароль",
                        modifier = Modifier.height(36.dp),
                        visualTransformation = if (isPasswordVisibleFirst) VisualTransformation.None else StarVisualTransformation(),
                        rightIcon = {
                            IconButton(onClick = { isPasswordVisibleFirst = !isPasswordVisibleFirst }) {
                                Icon(
                                    imageVector = if (isPasswordVisibleFirst) Icons.TwoTone.Info else Icons.TwoTone.Lock,
                                    contentDescription = "Toggle Password Visibility",
                                    modifier = Modifier
                                        .size(28.dp)
                                )
                            }
                        }
                    )
                    ThemedInputField(
                        textFieldValue = retryPasswordInput,
                        placeholder = "Повторите пароль",
                        modifier = Modifier.height(36.dp),
                        visualTransformation = StarVisualTransformation()
                    )
                    Button(
                        onClick = {
                            if (passwordInput.value != retryPasswordInput.value) {
                                registrationFormViewModel.setUserState(
                                    UiState.Error(message = "Пароли не совпадают")
                                )
                                return@Button
                            }
                            if (passwordInput.value.isEmpty()) {
                                registrationFormViewModel.setUserState(
                                    UiState.Error(message = "Пароль не может быть пустым")
                                )
                                return@Button
                            }
                            if (mailInput.value.isEmpty()) {
                                registrationFormViewModel.setUserState(
                                    UiState.Error(message = "Почта не может быть пустой")
                                )
                                return@Button
                            }
                            if (usernameInput.value.isEmpty()) {
                                registrationFormViewModel.setUserState(
                                    UiState.Error(message = "Имя пользователя не может быть пустым")
                                )
                                return@Button
                            }
                            if (usernameInput.value.length < 3) {
                                registrationFormViewModel.setUserState(
                                    UiState.Error(message = "Имя пользователя слишком короткое")
                                )
                                return@Button
                            }
                            coroutineScope.launch {
                                val isRegistrationSuccess = registrationFormViewModel.registration(
                                    username = usernameInput.value,
                                    mail = mailInput.value,
                                    password = passwordInput.value
                                )
                                if (isRegistrationSuccess) {
                                    navController.navigate(Screen.ProfileUserScreen.route)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Зарегистрироваться")
                    }
                    TextButton(
                        onClick = { navController.navigate(Screen.LoginForm.route) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "У меня уже есть аккаунт")
                    }
                }
            }
        }
    }
}