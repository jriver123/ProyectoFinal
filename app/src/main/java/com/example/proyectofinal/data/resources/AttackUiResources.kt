package com.example.proyectofinal.data.resources

import com.example.proyectofinal.R

private val attackSkillArtMap: Map<Int, Int> = mapOf(
    7 to R.drawable.sa_fireball1
)

fun getAttackSkillArtResId(attackId: Int): Int {
    return attackSkillArtMap[attackId] ?: R.drawable.sa_null
}

