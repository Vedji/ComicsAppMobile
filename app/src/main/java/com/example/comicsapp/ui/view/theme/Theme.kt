package com.example.comicsapp.ui.view.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.collection.emptyLongSet
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext




@Composable
fun ComicsAppTheme(
    // Dynamic color is available on Android 12+
    appThemeID: Int = 2,
    content: @Composable () -> Unit
) {
    // val colorScheme = when {
    //     // dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
    //     //     val context = LocalContext.current
    //     //     // if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    //     // }
    //     darkTheme -> DarkColorScheme
    //     else -> LightColorScheme
    // }
    val darkTheme: Boolean = isSystemInDarkTheme()
    val colorScheme = when (appThemeID){
        0 -> LightColorScheme
        1 -> DarkColorScheme
        2 -> TestLightColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    Log.d("ComicsAppTheme", "ComicsAppTheme isDarkTheme = $darkTheme")
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = CustomShapes,
        content = content
    )
}