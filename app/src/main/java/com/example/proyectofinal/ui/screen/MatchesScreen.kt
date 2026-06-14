package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.StatCard
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.MatchHistory
import com.example.proyectofinal.data.resources.t


@Composable
fun MatchesScreen(
    usuario: UsuarioUI,
    language: String,
    matches: List<MatchHistory>,
    navController: NavController,
    onPlayMatch: () -> Unit,
    onGoToStory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = t(language, "matches_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = t(language, "matches_description"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5F7A)
            )
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
                        text = "Modo Historia",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = "Capítulo actual: ${usuario.storyProgress} de 3. Elige uno de tus personajes para adentrarte en esta aventura, " +
                                "te han invitado a un club de peleas con Avatares, el famoso Battle.io. Ahora ve con todo y arrasa a la competencia! ",
                        color = Color(0xFF5F5F7A)
                    )
                    Button(
                        onClick = { onGoToStory() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("Entrar al modo historia")
                    }
                }
            }
        }

        item {
            Button(
                onClick = onPlayMatch,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A896))
            ) {
                Text(t(language, "play_simulated"))
            }
        }

        items(matches) { match ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = t(language, match.titleKey),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = t(language, match.resultKey),
                            color = Color(0xFF5F5F7A)
                        )
                    }
                    Text(
                        text = match.score,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00A896)
                    )
                }
            }
        }
    }
}
