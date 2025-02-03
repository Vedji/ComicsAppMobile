package com.example.comicsappmobile.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comicsappmobile.R
import com.example.comicsappmobile.navigation.Screen
import kotlinx.coroutines.delay


@Composable
fun TitleScreen(
    navController: NavController
) {
    val isDarkThemeActive = true // isSystemInDarkTheme()
    val background = if (isDarkThemeActive) Color.Black else Color.White
    val textColor = Color(0xFFFF6E79)
    val indicatorColor = Color(0xFFFFD2D2)

    LaunchedEffect(Unit) {
        delay(600)
        navController.navigate(Screen.Catalog.route)
    }

    Scaffold(
        containerColor = background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 64.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.app_main_icon),
                    contentDescription = "Preview icon",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Comics App",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = textColor
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator(
                    color = indicatorColor
                )
            }
        }
    }
}