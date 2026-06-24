package com.example.proyectofinal.data.resources

import com.example.proyectofinal.R

private val attackSkillArtMap: Map<Int, Int> = mapOf(
    1 to R.drawable.sa_punch1,
    2 to R.drawable.sa_slash1,
    3 to R.drawable.sa_headbutt1,
    4 to R.drawable.sa_kick1,
    5 to R.drawable.sa_throwing1,
    6 to R.drawable.sa_blast1,
    7 to R.drawable.sa_fireball1,
    8 to R.drawable.sa_spark1,
    9 to R.drawable.sa_thrusting1,
    10 to R.drawable.sa_critical1,
    11 to R.drawable.sa_heal1,
    12 to R.drawable.sa_heal2,
    13 to R.drawable.sa_shield1,
    14 to R.drawable.sa_shield2,
    15 to R.drawable.sa_finalbarrier1

)

fun getAttackSkillArtResId(attackId: Int): Int {
    return attackSkillArtMap[attackId] ?: R.drawable.sa_null
}

