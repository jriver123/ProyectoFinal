package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.OptionChip
import com.example.proyectofinal.SettingsCard
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.t
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    usuario: UsuarioUI,
    language: String,
    graphicsQuality: String,
    selectedLanguage: String,
    musicVolume: Float,
    soundVolume: Float,
    navController: NavController,
    onGraphicsQualityChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onMusicVolumeChange: (Float) -> Unit,
    onSoundVolumeChange: (Float) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = t(language, "settings_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
        }

        item {
            SettingsCard(title = t(language, "language")) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OptionChip("Español", selectedLanguage == "es") { onLanguageChange("es") }
                    OptionChip("English", selectedLanguage == "en") { onLanguageChange("en") }
                }
            }
        }

        item {
            SettingsCard(title = t(language, "graphics_quality")) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OptionChip(t(language, "quality_low"), graphicsQuality == "low") { onGraphicsQualityChange("low") }
                    OptionChip(t(language, "quality_medium"), graphicsQuality == "medium") { onGraphicsQualityChange("medium") }
                    OptionChip(t(language, "quality_high"), graphicsQuality == "high") { onGraphicsQualityChange("high") }
                }
            }
        }

        item {
            SettingsCard(title = t(language, "music_volume")) {
                Text("${musicVolume.roundToInt()}%", fontWeight = FontWeight.Bold)
                Slider(
                    value = musicVolume,
                    onValueChange = onMusicVolumeChange,
                    valueRange = 0f..100f
                )
            }
        }

        item {
            SettingsCard(title = t(language, "sound_volume")) {
                Text("${soundVolume.roundToInt()}%", fontWeight = FontWeight.Bold)
                Slider(
                    value = soundVolume,
                    onValueChange = onSoundVolumeChange,
                    valueRange = 0f..100f
                )
            }
        }

        // ✅ Botón para volver al Home usando NavController
        item {
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
