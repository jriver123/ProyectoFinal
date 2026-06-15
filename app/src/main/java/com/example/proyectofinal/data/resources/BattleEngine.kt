package com.example.proyectofinal.data.resources

data class BattleEngineState(
    val playerHp: Int,
    val enemyHpMap: Map<Int, Int>,
    val battleMessage: String,
    val battleFinished: Boolean,
    val isPlayerTurn: Boolean
)

data class BattleTurnOutcome(
    val state: BattleEngineState,
    val defeatedEnemyId: Int? = null,
    val allEnemiesDefeated: Boolean = false,
    val playerDefeated: Boolean = false
)

class BattleEngine {

    fun startBattle(player: Hero, enemies: List<Enemy>): BattleEngineState {
        val enemyHpMap = enemies.associate { it.id to it.HpStat }
        return BattleEngineState(
            playerHp = player.HpStat,
            enemyHpMap = enemyHpMap,
            battleMessage = "¡El combate comienza!",
            battleFinished = false,
            isPlayerTurn = true
        )
    }

    fun applyPlayerAttack(
        current: BattleEngineState,
        enemy: Enemy,
        damage: Int,
        actionMessage: String
    ): BattleTurnOutcome {
        if (current.battleFinished || !current.isPlayerTurn) {
            return BattleTurnOutcome(current)
        }

        val currentEnemyHp = current.enemyHpMap[enemy.id] ?: 0
        if (currentEnemyHp <= 0) {
            return BattleTurnOutcome(current)
        }

        val updatedEnemyHp = (currentEnemyHp - damage).coerceAtLeast(0)
        val updatedMap = current.enemyHpMap.toMutableMap().apply { put(enemy.id, updatedEnemyHp) }

        if (updatedEnemyHp <= 0) {
            val allDefeated = updatedMap.values.all { it <= 0 }
            val message = if (allDefeated) {
                "¡Has derrotado a todos los enemigos!"
            } else {
                "¡Has derrotado a ${enemy.name}!"
            }

            val newState = current.copy(
                enemyHpMap = updatedMap,
                battleMessage = message,
                battleFinished = allDefeated,
                isPlayerTurn = !allDefeated
            )
            return BattleTurnOutcome(
                state = newState,
                defeatedEnemyId = enemy.id,
                allEnemiesDefeated = allDefeated
            )
        }

        val newState = current.copy(
            enemyHpMap = updatedMap,
            battleMessage = actionMessage,
            isPlayerTurn = false
        )
        return BattleTurnOutcome(state = newState)
    }

    fun applyEnemyAttack(
        current: BattleEngineState,
        damage: Int,
        actionMessage: String
    ): BattleTurnOutcome {
        if (current.battleFinished) {
            return BattleTurnOutcome(current)
        }

        val updatedPlayerHp = (current.playerHp - damage).coerceAtLeast(0)
        if (updatedPlayerHp <= 0) {
            val defeatedState = current.copy(
                playerHp = 0,
                battleMessage = "¡Has sido derrotado!",
                battleFinished = true,
                isPlayerTurn = false
            )
            return BattleTurnOutcome(state = defeatedState, playerDefeated = true)
        }

        val newState = current.copy(
            playerHp = updatedPlayerHp,
            battleMessage = actionMessage,
            isPlayerTurn = true
        )
        return BattleTurnOutcome(state = newState)
    }
}

