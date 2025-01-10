package com.example.comicsapp.ui.view.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF87915C),
    onPrimary = Color(0xFFE6EDCD),
    secondary = Color(0xFF545B39),
    onSecondary = Color(0xFF9BA775),
    background = Color(0xFF1F201A),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFE2E5D4)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF87915C),    // 0xFF87915C
    onPrimary = Color(0xFFE5ECCA),  // 0xFFE5ECCA
    secondary = Color(0xFFC7CFA6),
    onSecondary = Color(0xFF485027),
    background = Color(0xFFE6ECD2),
    onBackground = Color(0xFF474C2C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF5B613F)
)

val TestLightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF6A8147), // Основной зеленый цвет для кнопок и заголовков
    onPrimary = Color.White, // Цвет текста на основном цвете
    primaryContainer = Color(0xFFCDDCC8), // Фоновые акценты
    onPrimaryContainer = Color(0xFF313A24), // Темный текст на акцентном фоне

    secondary = Color(0xFF6A8147), // Вторичный цвет (такой же, чтобы поддерживать стилистику)
    onSecondary = Color.White, // Текст на вторичном цвете
    secondaryContainer = Color(0xFFEFF5EB), // Светлый фон для вторичных элементов
    onSecondaryContainer = Color(0xFF4D5A38), // Текст на вторичном фоне

    background = Color(0xFFF7F9F4), // Основной цвет фона (светлый)
    onBackground = Color(0xFF313A24), // Цвет текста на фоне

    surface = Color(0xFFF7F9F4), // Цвет поверхности (карточек и т.д.)
    onSurface = Color(0xFF313A24), // Цвет текста на поверхности

    error = Color(0xFFBA1A1A), // Ошибки
    onError = Color.White // Текст на ошибках
)