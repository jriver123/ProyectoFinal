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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.*
import kotlin.collections.toIntArray

@Composable
fun BattleScreen(
    player: Hero,
    enemy: Enemy,
    chapter: StoryChapter,
    playerHp: Int,
    enemyHp: Int,
    battleMessage: String,
    battleFinished: Boolean,
    onAttack: (AttackMove) -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    // Convertimos los IDs en objetos AttackMove
    val playerAttacks = remember { getAttacksByIds(*player.attacks.toIntArray()) }
    val enemyAttacks = remember { getAttacksByIds(*enemy.attacks.toIntArray()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Combate por turnos", style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
            Text(chapter.title, color = Color(0xFF5F5F7A))
        }

        item {
            FighterCardHero("Tu personaje", player, playerHp, Color(0xFF00A896))
        }

        item {
            FighterCardEnemy("Enemigo", enemy, enemyHp, Color(0xFFE63946))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Text(battleMessage, modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (!battleFinished) {
            item {
                Text("Elige un ataque:", fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
            }

            items(playerAttacks) { attack ->
                AttackButton(attack = attack, onAttack = { onAttack(attack) })
            }
        } else {
            item {
                Button(onClick = onRetry, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)) { Text("Intentar otra vez") }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onExit, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)) { Text("Volver a partidas") }
            }
        }
    }
}
