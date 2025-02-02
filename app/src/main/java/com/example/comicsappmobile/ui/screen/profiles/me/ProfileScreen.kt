package com.example.comicsappmobile.ui.screen.profiles.me

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.navigation.Screen
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import com.example.comicsappmobile.ui.screen.profiles.me.cards.ProfileDescriptionCard
import com.example.comicsappmobile.ui.screen.profiles.me.tabs.ProfileAddedBooksTab
import com.example.comicsappmobile.ui.screen.profiles.me.tabs.ProfileCommentsTab
import com.example.comicsappmobile.ui.screen.profiles.me.tabs.ProfileFavoriteBooksTab
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = koinViewModel(),
    drawerState: DrawerState
) {
    var state by remember { mutableIntStateOf(0) }
    val titles =
        listOf(
            "Избранное",
            "Мои Комментарии",
            "Добавленные тайтлы"
        )
    val pageScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(64.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = // Navigate to profile editor screen
                            { navController.navigate(Screen.ProfileEditorScreen.route) },
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                "Edit profile",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (state == 2) {
                SmallFloatingActionButton(
                    onClick = { navController.navigate(Screen.EditedBookScreen.createRoute(-1)) },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier
                        .size(48.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.extraLarge
                        ),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Edit user profile information",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(pageScrollState)
            ) {
                ProfileDescriptionCard(navController, profileViewModel)
                OutlinedCard(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors().copy(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    SecondaryScrollableTabRow(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(
                                    selectedTabIndex = state,
                                    matchContentSize = false
                                ),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        divider = {
                            HorizontalDivider(
                                thickness = 3.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        selectedTabIndex = state
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = { Text(title) }
                            )
                        }
                    }
                    when (state) {
                        0 -> {
                            ProfileFavoriteBooksTab(
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }

                        1 -> {
                            ProfileCommentsTab(
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }

                        2 -> {
                            ProfileAddedBooksTab(
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }

                        else -> {
                            ThemedErrorCard()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}