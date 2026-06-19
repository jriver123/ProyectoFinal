package com.example.proyectofinal.ui.screen

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.*
import com.example.proyectofinal.ui.components.AttackCommandBar
import kotlin.collections.toIntArray

@Composable
fun BattleScreen(
    player: Hero,
    enemigos: List<Enemy>,
    chapter: StoryChapter,
    playerHp: Int,
    playerXP: Int,
    playerNextLevelXP: Int,
    playerLevel: Int,
    playerAttackIds: List<Int>,
    enemyHpMap: Map<Int, Int>,
    battleMessage: String,
    battleFinished: Boolean,
    isPlayerTurn: Boolean,
    isResolvingTurn: Boolean,
    requiresSkillSelection: Boolean,
    pendingSkillChoices: List<AttackMove>,
    soundCue: BattleSoundCue?,
    onSoundConsumed: () -> Unit,
    onAttack: (AttackMove, Int) -> Unit,
    onSelectSkill: (Int) -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    val playerAttacks = remember(playerAttackIds) { getAttacksByIds(*playerAttackIds.toIntArray()) }
    var selectedEnemyId by remember { mutableStateOf<Int?>(null) }
    val playSoundCue = rememberAttackSoundPlayer()
    val aliveEnemies = enemigos.filter { (enemyHpMap[it.id] ?: 0) > 0 }
    val aliveEnemyIds = aliveEnemies.map { it.id }.toSet()

    val animatedPlayerHp by animateIntAsState(
        targetValue = playerHp,
        animationSpec = tween(durationMillis = 650),
        label = "playerHpAnimation"
    )

    LaunchedEffect(soundCue) {
        val cue = soundCue ?: return@LaunchedEffect
        playSoundCue(cue)
        onSoundConsumed()
    }

    LaunchedEffect(aliveEnemyIds, selectedEnemyId) {
        if (selectedEnemyId != null && selectedEnemyId !in aliveEnemyIds) {
            selectedEnemyId = null
        }
    }

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
            val heroRows = buildFormationRows(listOf(player))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                heroRows.forEach { rowHeroes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                    ) {
                        rowHeroes.forEach { hero ->
                            val slotModifier = when {
                                rowHeroes.size == 1 && heroRows.size > 1 -> Modifier.fillMaxWidth(0.48f)
                                rowHeroes.size == 1 -> Modifier.fillMaxWidth()
                                else -> Modifier.weight(1f)
                            }

                            FighterCardHero(
                                title = "Tu personaje",
                                character = hero,
                                currentHp = animatedPlayerHp,
                                currentXP = playerXP,
                                nextLevelXP = playerNextLevelXP,
                                barColor = Color(0xFF0BA896),
                                modifier = slotModifier
                            )
                        }
                    }
                }
            }
        }

        item {
            val enemyRows = buildFormationRows(aliveEnemies)
            Column(
                modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 350)),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                enemyRows.forEach { rowEnemies ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                    ) {
                        rowEnemies.forEach { enemy ->
                            val hp = enemyHpMap[enemy.id] ?: 0
                            val animatedEnemyHp by animateIntAsState(
                                targetValue = hp,
                                animationSpec = tween(durationMillis = 650),
                                label = "enemyHpAnimation_${enemy.id}"
                            )
                            val slotModifier = when {
                                rowEnemies.size == 1 && enemyRows.size > 1 -> Modifier.fillMaxWidth(0.48f)
                                rowEnemies.size == 1 -> Modifier.fillMaxWidth()
                                else -> Modifier.weight(1f)
                            }

                            Box(
                                modifier = slotModifier
                                    .border(
                                        width = if (selectedEnemyId == enemy.id) 3.dp else 1.dp,
                                        color = if (selectedEnemyId == enemy.id) Color(0xFF3A0CA3) else Color.LightGray,
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .clickable(enabled = !isResolvingTurn) { selectedEnemyId = enemy.id }
                            ) {
                                FighterCardEnemy(
                                    title = enemy.name,
                                    character = enemy,
                                    currentHp = animatedEnemyHp,
                                    barColor = Color(0xFFE63946),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
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

        if (requiresSkillSelection) {
            item {
                Text("Elige una nueva habilidad", fontWeight = FontWeight.Bold, color = Color(0xFF3A8CA3))
            }

            items(pendingSkillChoices) { skill ->
                OutlinedButton(
                    onClick = { onSelectSkill(skill.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Aprender ${skill.name}")
                }
            }
        }

        // Barra de ataque con iconos (max 4)
        if (!battleFinished && isPlayerTurn && !requiresSkillSelection) {
            item {
                AttackCommandBar(
                    attacks = playerAttacks,
                    canUseAttacks = selectedEnemyId != null && !isResolvingTurn,
                    selectedEnemyName = aliveEnemies.firstOrNull { it.id == selectedEnemyId }?.name,
                    onAttackClick = { attack ->
                        val targetId = selectedEnemyId ?: return@AttackCommandBar
                        if (targetId !in aliveEnemyIds) return@AttackCommandBar
                        onAttack(attack, targetId)
                    }
                )
            }
        } else if (battleFinished && !requiresSkillSelection) {
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

private fun <T> buildFormationRows(items: List<T>): List<List<T>> {
    return when (items.size) {
        0 -> emptyList()
        1 -> listOf(items)
        2 -> listOf(items)
        3 -> listOf(listOf(items[0]), listOf(items[1], items[2]))
        4 -> listOf(items.take(2), items.drop(2))
        else -> items.chunked(2)
    }
}

