package com.example.comicsappmobile.ui.screen.examples

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun FontDisplayExamplesScreen() {
    val typography = MaterialTheme.typography
    val fontStyles = listOf(
        "Display Large" to typography.displayLarge,
        "Display Medium" to typography.displayMedium,
        "Display Small" to typography.displaySmall,
        "Headline Large" to typography.headlineLarge,
        "Headline Medium" to typography.headlineMedium,
        "Headline Small" to typography.headlineSmall,
        "Title Large" to typography.titleLarge,
        "Title Medium" to typography.titleMedium,
        "Title Small" to typography.titleSmall,
        "Body Large" to typography.bodyLarge,
        "Body Medium" to typography.bodyMedium,
        "Body Small" to typography.bodySmall,
        "Label Large" to typography.labelLarge,
        "Label Medium" to typography.labelMedium,
        "Label Small" to typography.labelSmall
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fontStyles.forEach { (name, style) ->
            item{
                FontItem(name = name, style = style)
            }
        }
        item{
            ColorDisplayScreen()
        }
        item {
            ShapeDisplayScreen()
        }
    }
}

@Composable
fun FontItem(name: String, style: TextStyle) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = style
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider()
    }
}


@Composable
fun ColorDisplayScreen() {
    val colors = listOf(
        "Primary" to MaterialTheme.colorScheme.primary,
        "On Primary" to MaterialTheme.colorScheme.onPrimary,
        "Primary Container" to MaterialTheme.colorScheme.primaryContainer,
        "On Primary Container" to MaterialTheme.colorScheme.onPrimaryContainer,
        "Secondary" to MaterialTheme.colorScheme.secondary,
        "On Secondary" to MaterialTheme.colorScheme.onSecondary,
        "Secondary Container" to MaterialTheme.colorScheme.secondaryContainer,
        "On Secondary Container" to MaterialTheme.colorScheme.onSecondaryContainer,
        "Tertiary" to MaterialTheme.colorScheme.tertiary,
        "On Tertiary" to MaterialTheme.colorScheme.onTertiary,
        "Tertiary Container" to MaterialTheme.colorScheme.tertiaryContainer,
        "On Tertiary Container" to MaterialTheme.colorScheme.onTertiaryContainer,
        "Error" to MaterialTheme.colorScheme.error,
        "On Error" to MaterialTheme.colorScheme.onError,
        "Error Container" to MaterialTheme.colorScheme.errorContainer,
        "On Error Container" to MaterialTheme.colorScheme.onErrorContainer,
        "Background" to MaterialTheme.colorScheme.background,
        "On Background" to MaterialTheme.colorScheme.onBackground,
        "Surface" to MaterialTheme.colorScheme.surface,
        "On Surface" to MaterialTheme.colorScheme.onSurface,
        "Surface Variant" to MaterialTheme.colorScheme.surfaceVariant,
        "On Surface Variant" to MaterialTheme.colorScheme.onSurfaceVariant,
        "Outline" to MaterialTheme.colorScheme.outline,
        "Inverse On Surface" to MaterialTheme.colorScheme.inverseOnSurface,
        "Inverse Surface" to MaterialTheme.colorScheme.inverseSurface
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { (name, color) ->
            ColorItem(name = name + "<Color(r = '${color.red * 255}', g = '${color.green * 255}', b = '${color.blue * 255}', a = '${color.alpha}')>", color = color)
        }
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color)
            .clipToBounds()
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if (color.luminance() > 0.5) Color.Black else Color.White),
                MaterialTheme.shapes.medium
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            color = if (color.luminance() > 0.5) Color.Black else Color.White,
            modifier = Modifier.padding(8.dp).wrapContentHeight()
        )
    }
}


@Composable
fun ShapeDisplayScreen() {
    val shapes = listOf(
        "Extra Small" to RoundedCornerShape(MaterialTheme.shapes.extraSmall.topStart),
        "Small" to RoundedCornerShape(MaterialTheme.shapes.small.topStart),
        "Medium" to RoundedCornerShape(MaterialTheme.shapes.medium.topStart),
        "Large" to RoundedCornerShape(MaterialTheme.shapes.large.topStart),
        "Extra Large" to RoundedCornerShape(MaterialTheme.shapes.extraLarge.topStart),
        "Circle" to CircleShape
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        shapes.forEach { (name, shape) ->
            ShapeItem(name = name, shape = shape)
        }
    }
}

@Composable
fun ShapeItem(name: String, shape: RoundedCornerShape) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = shape)
        )
    }
}