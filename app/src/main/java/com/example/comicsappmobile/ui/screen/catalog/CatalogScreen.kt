package com.example.comicsappmobile.ui.screen.catalog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.ui.components.ThemedSearchBar
import com.example.comicsappmobile.ui.screen.catalog.tabs.CatalogBooksTab
import com.example.comicsappmobile.ui.screen.catalog.tabs.CatalogFiltersTab
import com.example.comicsappmobile.ui.presentation.viewmodel.CatalogViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavHostController,
    catalogViewModel: CatalogViewModel = koinViewModel()
) {

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            catalogViewModel.removeFilters()
            textFieldValue = ""
            isRefreshing = false
            selectedTab = 0
        }
    }
    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize().border(1.dp, Color.Red),
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                if (selectedTab == 0) {

                    TopAppBar(
                        modifier = Modifier.padding(start = 0.dp),
                        scrollBehavior = scrollBehavior,
                        navigationIcon = { },
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        PaddingValues(
                                            start = (24 - 12).dp,
                                            end = 24.dp,
                                            top = 8.dp
                                        ),
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                ThemedSearchBar(
                                    text = textFieldValue,
                                    onTextChange = { textFieldValue = it },
                                    placeholder = "Введите текст",
                                    modifier = Modifier.width(256.dp),
                                    onClickSearch = { searchValue ->
                                        catalogViewModel.setSearch(searchValue)
                                    }
                                )
                                TooltipBox(
                                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
                                        spacingBetweenTooltipAndAnchor = (-20).dp
                                    ) ,
                                    tooltip = { PlainTooltip { Text("Фильтры и сортировка") } },
                                    state = rememberTooltipState()
                                ) {
                                    IconButton(onClick = { selectedTab = 2 }) {
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
            },
            content = { innerPadding ->
                when (selectedTab) {
                    0 -> CatalogBooksTab(navController, catalogViewModel, innerPadding)
                    2 -> CatalogFiltersTab(
                        navController,
                        catalogViewModel,
                        innerPadding,
                        {
                            coroutineScope.launch {
                                scrollBehavior.state.contentOffset = 0f // Сброс смещения
                            }
                            selectedTab = 0
                        }
                    ) // Filter
                    else -> {
                        Text(text = "No Realize selectedTab = ${selectedTab}")
                    }
                }

            }
        )
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun test(){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var basicTextField = rememberTextFieldState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BasicTextField(
                            state = basicTextField,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.size(128.dp, 36.dp)
                            ) {
                                Text( text = "Сортировка")
                            }
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.size(128.dp, 36.dp)
                            ) {
                                Text( text = "Фильтр")
                            }
                        }
                    }
                        },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val list = (0..75).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            }
        }
    )
}