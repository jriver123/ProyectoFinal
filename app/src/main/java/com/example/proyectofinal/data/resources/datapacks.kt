package com.example.proyectofinal.data.resources



data class GamePack(
    val id: Int,
    val nameKey: String,
    val priceUsd: Int,
    val coins: Int,
    val bonusKey: String
)

data class MatchHistory(
    val id: Int,
    val titleKey: String,
    val resultKey: String,
    val score: String
)
data class BattleCharacter(
    val id: Int,
    val name: String,
    val role: String,
    val maxHp: Int,
    val attackBonus: Int,
    val description: String,
    val attacks: List<AttackMove>
)

data class StoryChapter(
    val id: Int,
    val title: String,
    val description: String,
    val enemy: BattleCharacter,
    val rewardCoins: Int,
    val rewardXp: Int
)
data class AttackMove(
    val name: String,
    val damage: Int,
    val description: String
)