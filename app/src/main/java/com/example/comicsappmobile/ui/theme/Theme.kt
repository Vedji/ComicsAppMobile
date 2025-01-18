package com.example.comicsappmobile.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.comicsappmobile.di.GlobalState
import org.koin.compose.koinInject


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)


val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun ComicsAppMobileTheme(
    themeId: Int = 0,
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
    //     val context = LocalContext.current
    //     if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    // }

    val globalState: GlobalState = koinInject()
    val colorScheme: ColorScheme = globalState.appThemes[themeId]?.second ?: lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )



}