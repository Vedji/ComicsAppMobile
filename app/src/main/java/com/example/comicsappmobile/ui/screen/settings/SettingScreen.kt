package com.example.comicsappmobile.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.ui.presentation.viewmodel.SettingsViewModel
import com.example.comicsappmobile.ui.screen.settings.cards.ProfileSettingsCard
import com.example.comicsappmobile.ui.screen.settings.cards.SetAppThemeCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen (
    navController: NavHostController,
    // profileViewModel: ProfileViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
){
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 0.dp),
                navigationIcon = { },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider() ,
                            tooltip = { PlainTooltip { Text("Фильтры и сортировка") } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = { }) { // TODO: open nav
                                Icon(
                                    painter = painterResource(R.drawable.baseline_tune_24),
                                    "",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                },
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
            Spacer(modifier = Modifier.height(24.dp))
            SetAppThemeCard(settingsViewModel)
            ProfileSettingsCard(settingsViewModel, navController)
        }
    }
}