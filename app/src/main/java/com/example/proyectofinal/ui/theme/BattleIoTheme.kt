package com.example.proyectofinal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BattleIoTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Color(0xFF6C63FF),
        secondary = Color(0xFF00A896),
        tertiary = Color(0xFFFFB703),
        background = Color(0xFFF7F4FF),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1F1F2E)
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}