package com.example.proyectofinal

import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.CombatManager
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CombatManagerTest {

    private val attackAlwaysHits = AttackMove(
        id = 100,
        name = "Prueba",
        basedamage = 20,
        accuracy = 1.0,
        type = "Test",
        description = "Ataque para pruebas"
    )

    private val attackNeverHits = AttackMove(
        id = 101,
        name = "Fallo",
        basedamage = 999,
        accuracy = 0.0,
        type = "Test",
        description = "Debe fallar siempre"
    )

    private val hero = Hero(
        id = 1,
        name = "Heroe",
        role = "Tester",
        HpStat = 100,
        attackStat = 0,
        defenseStat = 1,
        luckStat = 1,
        description = "",
        attacks = listOf(100)
    )

    private val enemy = Enemy(
        id = 2,
        level = 1,
        name = "Enemigo",
        role = "Tester",
        rewardXp = 10,
        HpStat = 100,
        attackStat = 50,
        defenseStat = 90,
        luckStat = 1,
        description = "",
        attacks = listOf(100)
    )

    @Test
    fun `enemy attack uses enemy stats and deals damage`() {
        val manager = CombatManager(Random(1))

        val (damage, _) = manager.realizarAtaqueEnemigo(
            ataque = attackAlwaysHits,
            atacante = enemy,
            defensor = hero
        )

        assertTrue(damage > 0)
    }

    @Test
    fun `attack with zero accuracy always fails`() {
        val manager = CombatManager(Random(1))

        val (damage, message) = manager.realizarAtaqueJugador(
            ataque = attackNeverHits,
            atacante = hero,
            defensor = enemy
        )

        assertEquals(0, damage)
        assertTrue(message.contains("falla"))
    }
}

