package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.*
import kotlin.collections.toIntArray

@Composable
fun BattleScreen(
    player: Hero,
    enemigos: List<Enemy>,
    chapter: StoryChapter,
    playerHp: Int,
    enemyHpMap: Map<Int, Int>,
    battleMessage: String,
    battleFinished: Boolean,
    isPlayerTurn: Boolean,
    onAttack: (AttackMove, Int) -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    val playerAttacks = remember { getAttacksByIds(*player.attacks.toIntArray()) }
    var selectedEnemyId by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Combate por turnos", style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold, color = Color(0xFF3A8CA3))
            Text(chapter.title, color = Color(0xFF5F5F7A))
        }

        item {
            FighterCardHero(
                title = "Tu personaje",
                character = player,
                currentHp = playerHp,
                barColor = Color(0xFF0BA896)
            )
        }

        // Mostrar enemigos como cards seleccionables
        enemigos.forEach { enemy ->
            val hp = enemyHpMap[enemy.id] ?: 0
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedEnemyId = enemy.id } // <-- selección al tocar el card
                        .border(
                            width = if (selectedEnemyId == enemy.id) 3.dp else 1.dp,
                            color = if (selectedEnemyId == enemy.id) Color(0xFF3A0CA3) else Color.LightGray,
                            shape = RoundedCornerShape(18.dp)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    FighterCardEnemy(
                        title = enemy.name,
                        character = enemy,
                        currentHp = hp,
                        barColor = Color(0xFFE63946)
                    )
                }
            }
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

        // Lista de ataques SIEMPRE visible
        if (!battleFinished && isPlayerTurn) {
            item {
                Text("Elige un ataque", fontWeight = FontWeight.Bold, color = Color(0xFF3A8CA3))
            }

            items(playerAttacks) { attack ->
                Button(
                    onClick = {
                        if (selectedEnemyId != null) {
                            onAttack(attack, selectedEnemyId!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    enabled = selectedEnemyId != null // solo habilitado si hay enemigo seleccionado
                ) {
                    Text("Usar ${attack.name}" + if (selectedEnemyId != null) " contra ${enemigos.first { it.id == selectedEnemyId }.name}" else "")
                }
            }
        } else if (battleFinished) {
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
