package com.example.proyectofinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.data.resources.necesitaObjetivo
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.BattleEngine
import com.example.proyectofinal.data.resources.BattleEngineState
import com.example.proyectofinal.data.resources.BattleTurnTimings
import com.example.proyectofinal.data.resources.CombatManager
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero
import com.example.proyectofinal.data.resources.getAttacksByIds
import com.example.proyectofinal.data.resources.getUnlockableAttacksForHero
import com.example.proyectofinal.data.resources.giveXP
import com.example.proyectofinal.data.resources.maxTargets
import com.example.proyectofinal.ui.screen.BattleSoundCue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BattleViewModel(
    private val combatManager: CombatManager = CombatManager(),
    private val battleEngine: BattleEngine = BattleEngine()
) : ViewModel() {

    var playerHp by mutableStateOf(0)
    var playerXP by mutableStateOf(0)
    var playerNextLevelXP by mutableStateOf(100)
    var playerLevel by mutableStateOf(1)
    var playerAttacks by mutableStateOf<List<Int>>(emptyList())
    var enemyHpMap = mutableStateMapOf<Int, Int>()
    var battleMessage by mutableStateOf("¡El combate comienza!")
    var battleFinished by mutableStateOf(false)
    var isPlayerTurn by mutableStateOf(true)
    var isResolvingTurn by mutableStateOf(false)
    var pendingSkillChoices by mutableStateOf<List<AttackMove>>(emptyList())
    var requiresSkillSelection by mutableStateOf(false)
    var playerWon by mutableStateOf(false)
    var battleResultResolved by mutableStateOf(false)
    var userRewardRegistered by mutableStateOf(false)
    var soundCue by mutableStateOf<BattleSoundCue?>(null)

    private var playerShieldPercent: Double = 0.0

    private var engineState: BattleEngineState? = null

    private fun applyState(newState: BattleEngineState) {
        engineState = newState
        playerHp = newState.playerHp
        enemyHpMap.clear()
        enemyHpMap.putAll(newState.enemyHpMap)
        battleMessage = newState.battleMessage
        battleFinished = newState.battleFinished
        isPlayerTurn = newState.isPlayerTurn
    }

    private fun emitAttackSound(attackId: Int) {
        soundCue = BattleSoundCue.AttackHit(attackId = attackId)
    }

    private fun emitMissSound(attackId: Int) {
        soundCue = BattleSoundCue.AttackMiss(attackId = attackId)
    }

    fun consumeSoundCue() {
        soundCue = null
    }

    fun iniciarCombate(player: Hero, enemigos: List<Enemy>) {
        applyState(battleEngine.startBattle(player, enemigos))
        syncPlayerStats(player)
        pendingSkillChoices = emptyList()
        requiresSkillSelection = false
        playerWon = false
        battleResultResolved = false
        userRewardRegistered = false
        isResolvingTurn = false
        soundCue = null
        playerShieldPercent = 0.0
    }

    private fun syncPlayerStats(player: Hero) {
        playerXP = player.currentXP
        playerNextLevelXP = player.nextLevelXP
        playerLevel = player.level
        playerAttacks = player.attacks.toList()
    }

    fun atacar(player: Hero, enemigos: List<Enemy>, targetIds: List<Int>, ataque: AttackMove) {
        val currentState = engineState ?: return

        if (isResolvingTurn || !currentState.isPlayerTurn || currentState.battleFinished || requiresSkillSelection) {
            return
        }

        if (ataque.necesitaObjetivo()) {
            val requiredTargets = ataque.maxTargets()
            val validTargets = targetIds
                .distinct()
                .filter { (currentState.enemyHpMap[it] ?: 0) > 0 }

            if (validTargets.size < requiredTargets) {
                battleMessage = if (requiredTargets == 1) {
                    "Selecciona un enemigo vivo."
                } else {
                    "Selecciona $requiredTargets enemigos vivos."
                }
                return
            }
        }

        viewModelScope.launch {
            isResolvingTurn = true
            isPlayerTurn = false

            try {
                executePlayerTurn(player, enemigos, targetIds, ataque)
            } finally {
                isResolvingTurn = false
            }
        }
    }

    private suspend fun executePlayerTurn(
        player: Hero,
        enemigos: List<Enemy>,
        targetIds: List<Int>,
        ataque: AttackMove
    ) {
        val currentState = engineState ?: return

        battleMessage = "${player.name} usará ${ataque.name}..."
        delay(BattleTurnTimings.AttackAnnounceMs)

        if (!ataque.necesitaObjetivo()) {
            val newHp = (currentState.playerHp + ataque.healAmount).coerceAtMost(player.HpStat)

            if (ataque.shieldPercent > 0.0) {
                playerShieldPercent = maxOf(playerShieldPercent, ataque.shieldPercent)
            }

            val effectMessage = when {
                ataque.healAmount > 0 && ataque.shieldPercent > 0.0 ->
                    "${player.name} usa ${ataque.name}, recupera ${ataque.healAmount} de vida y activa un escudo."

                ataque.healAmount > 0 ->
                    "${player.name} usa ${ataque.name} y recupera ${ataque.healAmount} de vida."

                ataque.shieldPercent > 0.0 -> {
                    val percent = (ataque.shieldPercent * 100).toInt()
                    "${player.name} usa ${ataque.name} y reduce el próximo daño recibido en $percent%."
                }

                else ->
                    "${player.name} usa ${ataque.name}."
            }

            emitAttackSound(ataque.id)
            delay(BattleTurnTimings.SoundLeadMs)

            applyState(
                currentState.copy(
                    playerHp = newHp,
                    battleMessage = effectMessage,
                    isPlayerTurn = false
                )
            )

            delay(BattleTurnTimings.PostImpactMs)
            enemyTurn(player, enemigos)
            return
        }

        val targetEnemies = if (ataque.necesitaObjetivo()) {
            val requiredTargets = ataque.maxTargets()
            val validIds = targetIds
                .distinct()
                .filter { (currentState.enemyHpMap[it] ?: 0) > 0 }
                .take(requiredTargets)
            enemigos.filter { it.id in validIds }
        } else {
            emptyList()
        }

        if (ataque.necesitaObjetivo() && targetEnemies.isEmpty()) return

        var stateInTurn = currentState
        val defeatedEnemies = mutableListOf<Enemy>()
        var anyHit = false

        for (enemy in targetEnemies) {
            val (danio, mensaje) = combatManager.realizarAtaqueJugador(
                ataque,
                atacante = player,
                defensor = enemy
            )

            if (danio > 0) anyHit = true

            val playerOutcome = battleEngine.applyPlayerAttack(
                current = stateInTurn,
                enemy = enemy,
                damage = danio,
                actionMessage = mensaje
            )

            stateInTurn = playerOutcome.state
            if (playerOutcome.defeatedEnemyId == enemy.id) {
                defeatedEnemies.add(enemy)
            }
            if (playerOutcome.allEnemiesDefeated || stateInTurn.battleFinished) {
                break
            }
        }

        if (anyHit) emitAttackSound(ataque.id) else emitMissSound(ataque.id)

        delay(BattleTurnTimings.SoundLeadMs)

        applyState(stateInTurn)
        delay(BattleTurnTimings.PostImpactMs)

        if (defeatedEnemies.isNotEmpty()) {
            val previousLevel = player.level

            defeatedEnemies.forEach { defeatedEnemy ->
                giveXP(player, defeatedEnemy.rewardXp)
            }
            syncPlayerStats(player)

            if (player.level > previousLevel) {
                pendingSkillChoices = getUnlockableAttacksForHero(player).take(3)
                requiresSkillSelection = pendingSkillChoices.isNotEmpty()

                if (requiresSkillSelection) {
                    battleMessage = "${player.name} subió a nivel ${player.level}.\nElige una habilidad nueva."
                }
            }
        }

        if (stateInTurn.battleFinished) {
            playerWon = true
            battleResultResolved = true
            return
        }

        if (requiresSkillSelection) {
            return
        }

        enemyTurn(player, enemigos)
    }

    fun seleccionarNuevaHabilidad(player: Hero, attackId: Int) {
        if (!requiresSkillSelection) return

        val skill = pendingSkillChoices.firstOrNull { it.id == attackId } ?: return
        if (skill.id !in player.attacks) {
            if (player.attacks.size >= 4) {
                player.attacks = player.attacks.drop(1) + skill.id
            } else {
                player.attacks = player.attacks + skill.id
            }
        }

        requiresSkillSelection = false
        pendingSkillChoices = emptyList()
        battleMessage = "${player.name} aprendio ${skill.name}."
        syncPlayerStats(player)
    }

    private suspend fun enemyTurn(player: Hero, enemigos: List<Enemy>) {
        val currentState = engineState ?: return
        if (currentState.battleFinished) return

        val enemigosVivos = enemigos.filter { (currentState.enemyHpMap[it.id] ?: 0) > 0 }
        var stateInTurn = currentState

        for (enemigo in enemigosVivos) {
            val ataque = getAttacksByIds(*enemigo.attacks.toIntArray()).random()
            battleMessage = "${enemigo.name} usara ${ataque.name}..."
            isPlayerTurn = false
            delay(BattleTurnTimings.AttackAnnounceMs)

            val (danio, mensaje) = combatManager.realizarAtaqueEnemigo(
                ataque,
                atacante = enemigo,
                defensor = player
            )

            var finalDamage = danio
            var finalMessage = mensaje

            if (playerShieldPercent > 0.0 && finalDamage > 0) {
                val reducedDamage = (finalDamage * playerShieldPercent).toInt()
                finalDamage = (finalDamage - reducedDamage).coerceAtLeast(0)
                finalMessage = "$mensaje. El escudo reduce $reducedDamage de daño."
                playerShieldPercent = 0.0
            }

            if (finalDamage == 0) {
                emitMissSound(ataque.id)
            } else {
                emitAttackSound(ataque.id)
            }

            delay(BattleTurnTimings.SoundLeadMs)

            val enemyOutcome = battleEngine.applyEnemyAttack(
                current = stateInTurn,
                damage = finalDamage,
                actionMessage = finalMessage
            )

            applyState(enemyOutcome.state)
            stateInTurn = enemyOutcome.state
            delay(BattleTurnTimings.PostImpactMs)

            if (enemyOutcome.playerDefeated || stateInTurn.battleFinished) {
                playerWon = false
                battleResultResolved = true
                return
            }
        }
    }

    fun registrarRecompensaUsuario() {
        userRewardRegistered = true
    }

    fun reiniciar(player: Hero, enemigos: List<Enemy>) {
        iniciarCombate(player, enemigos)
    }
}
