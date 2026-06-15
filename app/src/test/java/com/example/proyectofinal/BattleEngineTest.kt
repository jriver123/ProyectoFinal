package com.example.proyectofinal

import com.example.proyectofinal.data.resources.BattleEngine
import com.example.proyectofinal.data.resources.Enemy
import com.example.proyectofinal.data.resources.Hero
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BattleEngineTest {

    private val hero = Hero(
        id = 1,
        name = "Hero",
        role = "Tester",
        HpStat = 100,
        attackStat = 10,
        defenseStat = 5,
        luckStat = 1,
        description = "",
        attacks = listOf(1)
    )

    private val enemy = Enemy(
        id = 7,
        level = 1,
        name = "Enemy",
        role = "Tester",
        rewardXp = 5,
        HpStat = 30,
        attackStat = 8,
        defenseStat = 2,
        luckStat = 1,
        description = "",
        attacks = listOf(1)
    )

    @Test
    fun `player attack defeats last enemy and ends battle`() {
        val engine = BattleEngine()
        val initial = engine.startBattle(hero, listOf(enemy))

        val outcome = engine.applyPlayerAttack(
            current = initial,
            enemy = enemy,
            damage = 99,
            actionMessage = "Golpe"
        )

        assertTrue(outcome.allEnemiesDefeated)
        assertTrue(outcome.state.battleFinished)
        assertEquals(0, outcome.state.enemyHpMap[enemy.id])
    }

    @Test
    fun `enemy attack can defeat player`() {
        val engine = BattleEngine()
        val initial = engine.startBattle(hero, listOf(enemy)).copy(playerHp = 10, isPlayerTurn = false)

        val outcome = engine.applyEnemyAttack(
            current = initial,
            damage = 15,
            actionMessage = "Contraataque"
        )

        assertTrue(outcome.playerDefeated)
        assertTrue(outcome.state.battleFinished)
        assertEquals(0, outcome.state.playerHp)
    }

    @Test
    fun `normal player attack passes turn to enemies`() {
        val engine = BattleEngine()
        val initial = engine.startBattle(hero, listOf(enemy))

        val outcome = engine.applyPlayerAttack(
            current = initial,
            enemy = enemy,
            damage = 5,
            actionMessage = "Golpe"
        )

        assertFalse(outcome.state.battleFinished)
        assertFalse(outcome.state.isPlayerTurn)
        assertEquals(25, outcome.state.enemyHpMap[enemy.id])
    }
}

