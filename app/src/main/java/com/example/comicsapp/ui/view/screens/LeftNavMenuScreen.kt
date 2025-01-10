package com.example.comicsapp.ui.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun LeftNavMenuScreen(navController: NavHostController, onCloseDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Меню", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        DrawerItem("Каталог") {
            navController.navigate("book_catalog")
            onCloseDrawer() // Закрываем Drawer после перехода
        }
        DrawerItem("Профиль") {
            navController.navigate("profile")
            onCloseDrawer()
        }
        DrawerItem("Настройки") {
            navController.navigate("settings")
            onCloseDrawer()
        }
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}