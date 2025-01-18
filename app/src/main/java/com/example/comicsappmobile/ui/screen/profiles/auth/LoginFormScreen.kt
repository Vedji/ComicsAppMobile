package com.example.comicsappmobile.ui.screen.profiles.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.utils.Logger
import com.example.comicsappmobile.ui.presentation.viewmodel.LoginFormViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginFormScreen (
    navController: NavHostController,
    loginFormViewModel: LoginFormViewModel = koinViewModel()
){
    val userLoginV2 = loginFormViewModel.userLogin.collectAsState()
    val checkAuth = loginFormViewModel.globalState.authUser.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val appContext = LocalContext.current.applicationContext

    val loginInput = rememberSaveable { mutableStateOf("") }
    val passwordInput = rememberSaveable { mutableStateOf("") }



    LaunchedEffect(userLoginV2.value) {
        delay(200)
        if (loginFormViewModel.globalState.authUser.value.userId > 0){
            navController.navigate(Screen.ProfileUserScreen.route)
        }
    }


    Scaffold(){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 36.dp)
        ){

            Spacer(modifier = Modifier
                .height(180.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth().align(Alignment.CenterHorizontally),
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
                            fontWeight = FontWeight.Light
                        ),
                        textAlign = TextAlign.Center
                    )
                    // TODO: Vew on error
                    Text(
                        text =
                        when(userLoginV2.value)
                        {
                            is UiState.Error -> {
                                if (userLoginV2.value.typeError == appContext.getString(R.string.AuthorizationError))
                                    "Логин или пароль введены неправильно"
                                else
                                    userLoginV2.value.message ?: "Ошибка приложения"
                            }
                            is UiState.Success -> { "Успешная авторизация" }
                            is UiState.Loading -> { "Проверка авторизации" }
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
                        textFieldValue =  loginInput,
                        placeholder = "Логин",
                        modifier = Modifier
                    )
                    ThemedInputField(
                        textFieldValue =  passwordInput,
                        placeholder = "Пароль",
                        modifier = Modifier
                    )
                    Button(
                        onClick = {
                            if (loginInput.value.length < 3 || passwordInput.value.length < 3){
                                loginFormViewModel.setUserAuthError(
                                    message = "Логин или пароль слишком короткие!",
                                    typeError = appContext.getString(R.string.AuthorizationError))
                                return@Button
                            }
                            coroutineScope.launch {
                                loginFormViewModel.loginFromUsername(
                                    username = loginInput.value,
                                    password = passwordInput.value
                                )
                                // TODO: Go to profile screen
                            } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text( text = "Войти")
                    }
                    // TODO: Go to register screen
                    TextButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text( text = "Зарегистрироваться")
                    }
                }
            }
        }
    }
}



// TODO: Move to components
@Composable
fun ThemedInputField(
    textFieldValue: MutableState<String> = rememberSaveable { mutableStateOf("") },
    placeholder: String = "Simple ThemedInputField",
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    placeholderStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f)
    ),
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    cursorColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    rightIcon: @Composable (() -> Unit)? = null
) {

    BasicTextField(
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        singleLine = true,
        modifier = modifier
            .height(36.dp)
            .clip(MaterialTheme.shapes.large)
            .background(containerColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (rightIcon != null) 8.dp else 0.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textFieldValue.value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = placeholderStyle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }

                rightIcon?.let {
                    it()
                }
            }
        }
    )
}
