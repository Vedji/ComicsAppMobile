package com.example.comicsappmobile.ui.screen.settings.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.presentation.viewmodel.SettingsViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.UiState
import kotlinx.coroutines.launch


@Composable
fun ProfileSettingsCard(
    settingsViewModel: SettingsViewModel,
    navController: NavController
) {
    val userLoginUi by settingsViewModel.userLogin.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        if (userLoginUi is UiState.Success && userLoginUi.data != null && userLoginUi.data?.userId!! > 0)
            Column(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
            ) {

                TextButton(
                    onClick = { coroutineScope.launch { settingsViewModel.outFromUserLogin() } },
                    modifier = Modifier.fillMaxWidth()
                    ) { Text(text = "Выйти из профиля") }
            }
        else
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                TextButton(
                    onClick = { navController.navigate(Screen.LoginForm.route) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = "Авторизироваться") }    // TODO: go to LoginFormScreen
            }
    }
}