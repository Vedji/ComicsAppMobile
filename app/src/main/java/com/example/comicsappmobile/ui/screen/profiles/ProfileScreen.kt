package com.example.comicsappmobile.ui.screen.profiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.ui.components.ThemedErrorCard
import com.example.comicsappmobile.ui.screen.profiles.tabs.ProfileCommentsTab
import com.example.comicsappmobile.ui.screen.profiles.cards.ProfileDescriptionCard
import com.example.comicsappmobile.ui.screen.profiles.tabs.ProfileAddedBooksTab
import com.example.comicsappmobile.ui.screen.profiles.tabs.ProfileFavoriteBooksTab
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme
import com.example.comicsappmobile.ui.presentation.viewmodel.CatalogViewModel
import com.example.comicsappmobile.ui.presentation.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val pageScrollState = rememberScrollState()
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues).fillMaxSize()
        ) {
            Column(modifier = Modifier
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
                    var state by remember { mutableStateOf(2) }
                    val titles =
                        listOf(
                            "Избранное",
                            "Мои Комментарии",
                            "Добавленные тайтлы"
                        )
                    SecondaryScrollableTabRow(
                        containerColor = MaterialTheme.colorScheme.primary,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTabIndex = state, matchContentSize = false),
                            color = MaterialTheme.colorScheme.secondary) },
                        divider = { HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.background) },
                        selectedTabIndex = state) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = { Text(title) }
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Scrolling secondary tab ${state + 1} selected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    when(state){
                        0 -> { ProfileFavoriteBooksTab(navController, profileViewModel) }
                        1 -> { ProfileCommentsTab(navController, profileViewModel) }
                        2 -> { ProfileAddedBooksTab() }
                        else -> { ThemedErrorCard()}
                    }
                }
            }
        }
    }
}