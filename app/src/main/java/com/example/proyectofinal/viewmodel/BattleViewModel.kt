package com.example.proyectofinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.CombatManager
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero
import com.example.proyectofinal.data.resources.getAttacksByIds
import androidx.compose.runtime.mutableStateMapOf
import com.example.proyectofinal.data.resources.giveXP

class BattleViewModel : ViewModel() {
    private val combatManager = CombatManager()

    var playerHp by mutableStateOf(0)
    var enemyHpMap = mutableStateMapOf<Int, Int>() // HP por enemigo
    var battleMessage by mutableStateOf("¡El combate comienza!")
    var battleFinished by mutableStateOf(false)
    var isPlayerTurn by mutableStateOf(true)

    fun iniciarCombate(player: Hero, enemigos: List<Enemy>) {
        playerHp = player.HpStat
        enemyHpMap.clear()
        enemigos.forEach { enemyHpMap[it.id] = it.HpStat }
        battleMessage = "¡El combate comienza!"
        battleFinished = false
        isPlayerTurn = true
    }

    fun atacar(player: Hero, enemigos: List<Enemy>, targetId: Int, ataque: AttackMove) {
        if (!isPlayerTurn || battleFinished) return

        val enemy = enemigos.firstOrNull { it.id == targetId } ?: return
        val (danio, mensaje) = combatManager.realizarAtaque(ataque, atacante = player, defensor = enemy)

        enemyHpMap[targetId] = (enemyHpMap[targetId]!! - danio).coerceAtLeast(0)
        battleMessage = mensaje

        if (enemyHpMap[targetId]!! <= 0) {
            battleMessage = "¡Has derrotado a ${enemy.name}!"

            // 🔹 Otorgar XP al héroe al derrotar enemigo
            giveXP(player, enemy.rewardXp)

            if (enemyHpMap.values.all { it == 0 }) {
                battleFinished = true
                battleMessage = "¡Has derrotado a todos los enemigos!"
                return
            }
        }

        isPlayerTurn = false
        enemyTurn(player, enemigos)
    }

    private fun enemyTurn(player: Hero, enemigos: List<Enemy>) {
        if (battleFinished) return

        val enemigosVivos = enemigos.filter { enemyHpMap[it.id]!! > 0 }
        for (enemigo in enemigosVivos) {
            val ataque = getAttacksByIds(*enemigo.attacks.toIntArray()).random()
            val (danio, mensaje) = combatManager.realizarAtaque(ataque, atacante = player, defensor = enemigo)

            playerHp = (playerHp - danio).coerceAtLeast(0)
            battleMessage = mensaje

            if (playerHp <= 0) {
                battleMessage = "¡Has sido derrotado!"
                battleFinished = true
                return
            }
        }

        isPlayerTurn = true
    }

    fun reiniciar(player: Hero, enemigos: List<Enemy>) {
        iniciarCombate(player, enemigos)
    }
}
