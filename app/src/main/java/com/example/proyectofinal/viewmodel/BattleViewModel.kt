package com.example.proyectofinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.CombatManager
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero

class BattleViewModel : ViewModel() {
    private val combatManager = CombatManager()

    var playerHp by mutableStateOf(0)
    var enemyHp by mutableStateOf(0)
    var battleMessage by mutableStateOf("¡El combate comienza!")
    var battleFinished by mutableStateOf(false)

    fun iniciarCombate(player: Hero, enemy: Enemy) {
        playerHp = player.HpStat
        enemyHp = enemy.HpStat
        battleMessage = "¡El combate comienza!"
        battleFinished = false
    }

    fun atacar(player: Hero, enemy: Enemy, ataque: AttackMove) {
        val (danio, mensaje) = combatManager.realizarAtaque(ataque, player, enemy)
        enemyHp = (enemyHp - danio).coerceAtLeast(0)
        battleMessage = mensaje

        if (enemyHp <= 0) {
            battleMessage = "¡Has derrotado al enemigo!"
            battleFinished = true
        }
    }

    fun reiniciar(player: Hero, enemy: Enemy) {
        iniciarCombate(player, enemy)
    }
}

