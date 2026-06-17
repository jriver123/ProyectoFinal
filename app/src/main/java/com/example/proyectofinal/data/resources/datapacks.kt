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
data class Hero(
    val id: Int,
    var level: Int = 1,
    var currentXP: Int = 0,
    var nextLevelXP: Int = 100,
    val name: String,
    val role: String,
    var HpStat: Int,
    var attackStat: Int,
    var defenseStat: Int,
    var luckStat: Int,
    val description: String,
    var attacks: List<Int>,
    val imageResId: Int = 0  // ID del recurso drawable para el portrait
)

data class Enemy(
    val id: Int,
    val level: Int,
    val name: String,
    val role: String,
    val rewardXp: Int,
    val HpStat: Int,
    val attackStat: Int,
    val defenseStat: Int,
    val luckStat: Int,
    val description: String,
    val attacks: List<Int>,
    val imageResId: Int = 0  // ID del recurso drawable para el portrait
)

data class StoryChapter(
    val id: Int,
    val title: String,
    val description: String,
    val enemies: List<Enemy>,
    val rewardCoins: Int,
    val rewardXp: Int
)
data class AttackMove(
    val id : Int,
    val name: String,
    val basedamage: Int,
    val accuracy: Double,
    val type: String,
    val description: String
)