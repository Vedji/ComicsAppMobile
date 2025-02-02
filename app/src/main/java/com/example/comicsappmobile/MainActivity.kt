package com.example.comicsappmobile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.comicsappmobile.data.repository.UserRepository
import com.example.comicsappmobile.di.GlobalState
import com.example.comicsappmobile.di.RetrofitInstance
import com.example.comicsappmobile.di.SharedViewModel
import com.example.comicsappmobile.navigation.AppNavHost
import com.example.comicsappmobile.navigation.ModelNavigationDrawer
import com.example.comicsappmobile.ui.theme.ComicsAppMobileTheme
import com.example.comicsappmobile.utils.Logger
import org.koin.compose.koinInject


class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val globalState: GlobalState = koinInject()
            val themeId by globalState.getAppThemeId().collectAsState(0)

            if (!globalState.firstUserAuth) {
                LaunchedEffect(Unit) {
                    globalState.getUserAccessToken().collect {
                        if (it == null || globalState.firstUserAuth)
                            return@collect  // TODO Replace to fetch
                        RetrofitInstance.accessToken = it
                        Logger.debug(
                            "LoginFormViewModel -> onCreate",
                            "globalState.getUserAccessToken().collect = '$it'"
                        )
                        val repo = UserRepository(
                            RetrofitInstance.userApi,
                            sharedViewModel = SharedViewModel(),
                            globalState = globalState
                        )
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
                        ModelNavigationDrawer(navController = navController, drawerState = drawerState)
                    },
                    scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                ) {
                    Scaffold(
                        topBar = { }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            drawerState = drawerState
                        )
                    }
                }
            }
        }
    }
}
