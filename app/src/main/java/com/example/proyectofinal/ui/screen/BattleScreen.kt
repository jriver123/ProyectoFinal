package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.proyectofinal.data.resources.*

@Composable
fun BattleScreen(
    player: BattleCharacter,
    enemy: BattleCharacter,
    chapter: StoryChapter,
    playerHp: Int,
    enemyHp: Int,
    battleMessage: String,
    battleFinished: Boolean,
    onAttack: (AttackMove) -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Combate por turnos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = chapter.title,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            FighterCard(
                title = "Tu personaje",
                character = player,
                currentHp = playerHp,
                barColor = Color(0xFF00A896)
            )
        }

        item {
            FighterCard(
                title = "Enemigo",
                character = enemy,
                currentHp = enemyHp,
                barColor = Color(0xFFE63946)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Text(
                    text = battleMessage,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (!battleFinished) {
            item {
                Text(
                    text = "Elige un ataque:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A0CA3)
                )
            }

            items(player.attacks) { attack ->
                AttackButton(attack = attack, onAttack = { onAttack(attack) })
            }
        } else {
            item {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Intentar otra vez")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Volver a partidas")
                }
            }
        }
    }
}