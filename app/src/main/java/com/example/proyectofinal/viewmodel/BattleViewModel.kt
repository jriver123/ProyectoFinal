package com.example.proyectofinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.BattleEngine
import com.example.proyectofinal.data.resources.BattleEngineState
import com.example.proyectofinal.data.resources.CombatManager
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero
import com.example.proyectofinal.data.resources.getAttacksByIds
import com.example.proyectofinal.data.resources.getUnlockableAttacksForHero
import com.example.proyectofinal.data.resources.giveXP

class BattleViewModel(
    private val combatManager: CombatManager = CombatManager(),
    private val battleEngine: BattleEngine = BattleEngine()
) : ViewModel() {

    var playerHp by mutableStateOf(0)
    var enemyHpMap = mutableStateMapOf<Int, Int>() // HP por enemigo
    var battleMessage by mutableStateOf("¡El combate comienza!")
    var battleFinished by mutableStateOf(false)
    var isPlayerTurn by mutableStateOf(true)
    var pendingSkillChoices by mutableStateOf<List<AttackMove>>(emptyList())
    var requiresSkillSelection by mutableStateOf(false)
    var playerWon by mutableStateOf(false)
    var userRewardRegistered by mutableStateOf(false)

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

    fun iniciarCombate(player: Hero, enemigos: List<Enemy>) {
        applyState(battleEngine.startBattle(player, enemigos))
        pendingSkillChoices = emptyList()
        requiresSkillSelection = false
        playerWon = false
        userRewardRegistered = false
    }

    fun atacar(player: Hero, enemigos: List<Enemy>, targetId: Int, ataque: AttackMove) {
        val currentState = engineState ?: return
        if (!currentState.isPlayerTurn || currentState.battleFinished || requiresSkillSelection) return

        val enemy = enemigos.firstOrNull { it.id == targetId } ?: return
        val (danio, mensaje) = combatManager.realizarAtaqueJugador(ataque, atacante = player, defensor = enemy)

        val playerOutcome = battleEngine.applyPlayerAttack(
            current = currentState,
            enemy = enemy,
            damage = danio,
            actionMessage = mensaje
        )
        applyState(playerOutcome.state)

        if (playerOutcome.defeatedEnemyId != null) {
            val previousLevel = player.level
            giveXP(player, enemy.rewardXp)

            if (player.level > previousLevel) {
                pendingSkillChoices = getUnlockableAttacksForHero(player).take(3)
                requiresSkillSelection = pendingSkillChoices.isNotEmpty()
                if (requiresSkillSelection) {
                    battleMessage = "${player.name} subio a nivel ${player.level}. Elige una habilidad nueva."
                }
            }
        }

        if (playerOutcome.allEnemiesDefeated || playerOutcome.state.battleFinished) {
            playerWon = true
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
    }

    private fun enemyTurn(player: Hero, enemigos: List<Enemy>) {
        val currentState = engineState ?: return
        if (currentState.battleFinished) return

        val enemigosVivos = enemigos.filter { (currentState.enemyHpMap[it.id] ?: 0) > 0 }
        var stateInTurn = currentState

        for (enemigo in enemigosVivos) {
            val ataque = getAttacksByIds(*enemigo.attacks.toIntArray()).random()
            val (danio, mensaje) = combatManager.realizarAtaqueEnemigo(ataque, atacante = enemigo, defensor = player)

            val enemyOutcome = battleEngine.applyEnemyAttack(
                current = stateInTurn,
                damage = danio,
                actionMessage = mensaje
            )
            applyState(enemyOutcome.state)
            stateInTurn = enemyOutcome.state

            if (enemyOutcome.playerDefeated || stateInTurn.battleFinished) {
                playerWon = false
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
