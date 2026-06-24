package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.preferences.ConfigManager
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.components.OptionChip
import com.example.proyectofinal.ui.components.SettingsCard
import com.example.proyectofinal.ui.navigation.AppRoutes
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
    val baseUrlState = remember { mutableStateOf(ConfigManager.getBaseUrl()) }

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

        // ✅ Sección de configuración de servidor (BASE_URL)
        item {
            SettingsCard(title = "🔧 Configuración de Servidor") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "URL del Servidor",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = baseUrlState.value,
                        onValueChange = { baseUrlState.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("http://192.168.0.243:8080/api/") },
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                ConfigManager.setBaseUrl(baseUrlState.value)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("✅ Guardar")
                        }

                        Button(
                            onClick = {
                                ConfigManager.resetBaseUrl()
                                baseUrlState.value = ConfigManager.getBaseUrl()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("🔄 Restaurar")
                        }
                    }

                    Text(
                        "URL actual: ${ConfigManager.getBaseUrl()}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // ✅ Botón para volver al Home usando NavController
        item {
            Button(
                onClick = { navController.navigate(AppRoutes.Home) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
