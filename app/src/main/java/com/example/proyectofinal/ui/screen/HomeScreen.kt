package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.StatCard
import com.example.proyectofinal.StoryProgressCard
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.rememberImageBitmap


@Composable
fun HomeScreen(
    usuario: UsuarioUI,
    language: String,
    profileImageUri: String?,
    onGoToStore: () -> Unit,
    onGoToProfile: () -> Unit,
    onGoToStory: () -> Unit
) {
    val imageBitmap = rememberImageBitmap(profileImageUri)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Battle.io",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Fragmentos del Núcleo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF6C63FF), Color(0xFFFFB3C6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = t(language, "profile_photo"),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("🎮", style = MaterialTheme.typography.headlineLarge)
                        }
                    }

                    Text(
                        text = usuario.username,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Nivel ${usuario.nivel} • ${usuario.monedas} monedas",
                        color = Color(0xFF5F5F7A)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = t(language, "victories"),
                    value = usuario.partidasGanadas.toString(),
                    subtitle = t(language, "won_sub"),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = t(language, "total"),
                    value = usuario.partidasJugadas.toString(),
                    subtitle = t(language, "played_sub"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            StoryProgressCard(storyProgress = usuario.storyProgress, onGoToStory = onGoToStory)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = t(language, "quick_actions"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Button(
                        onClick = onGoToStory,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Jugar modo historia")
                    }
                    OutlinedButton(
                        onClick = onGoToStore,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "go_store"))
                    }
                    OutlinedButton(
                        onClick = onGoToProfile,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "edit_profile"))
                    }
                }
            }
        }
    }
}
