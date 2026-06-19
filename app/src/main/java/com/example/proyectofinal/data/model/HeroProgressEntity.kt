package com.example.proyectofinal.data.model

import androidx.room.Entity

@Entity(
    tableName = "hero_progress",
    primaryKeys = ["userId", "heroId"]
)
data class HeroProgressEntity(
    val userId: Long,
    val heroId: Int,
    val level: Int,
    val currentXP: Int,
    val nextLevelXP: Int,
    val hpStat: Int,
    val attackStat: Int,
    val defenseStat: Int,
    val luckStat: Int,
    val attacksCsv: String
)

