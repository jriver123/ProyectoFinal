package com.example.proyectofinal.data.resources

import kotlin.random.Random

fun generateRandomEnemySquad(
    minEnemies: Int = 1,
    maxEnemies: Int = 4,
    random: Random = Random.Default
): List<Enemy> {
    val pool = GetEnemies()
    if (pool.isEmpty()) return emptyList()

    val count = random.nextInt(minEnemies, maxEnemies + 1)
    return List(count) { index ->
        val base = pool[random.nextInt(pool.size)]
        // Ensure unique ids per instance so selection/damage is isolated in battle UI.
        base.copy(id = 9000 + index)
    }
}

fun createQuickBattleChapter(enemies: List<Enemy>): StoryChapter {
    val normalizedCount = enemies.size.coerceIn(1, 4)
    return StoryChapter(
        id = 0,
        title = "Partida rápida",
        description = "Combate contra enemigos aleatorios",
        enemies = enemies,
        rewardCoins = 60 * normalizedCount,
        rewardXp = 35 * normalizedCount
    )
}

