package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.getStoryChapters

@Composable
fun StoryScreen(
    storyProgress: Int,
    onStart: () -> Unit,
    onBack: () -> Unit,
    onResetStory: () -> Unit
) {
    val chapters = getStoryChapters()
    val chapter = chapters.firstOrNull { it.id == storyProgress } ?: chapters.last()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Modo Historia",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Battle.io: Fragmentos del Núcleo",
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = chapter.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF34344A)
                    )
                    Text(
                        text = "Enemigo: ${chapter.enemy.name}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Recompensa: ${chapter.rewardCoins} monedas • ${chapter.rewardXp} XP",
                        color = Color(0xFF00A896),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Sinopsis general",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = "En el mundo digital de Battle.io, una falla del sistema creó enemigos llamados Glitches. Tu misión es entrar a la arena, elegir un campeón y recuperar los fragmentos del núcleo mediante combates por turnos.",
                        color = Color(0xFF34344A)
                    )
                }
            }
        }

        item {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Elegir personaje")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Volver a partidas")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onResetStory,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Reiniciar historia")
            }
        }
    }
}