package com.example.proyectofinal.data.resources

import com.example.proyectofinal.R


fun getPlayableCharacters(): List<Hero> {

    return listOf(
        Hero(
            id = 1,
            name = "Kael",
            level = 1,
            currentXP = 0,
            role = "Guerrero del Núcleo",
            HpStat = 125,
            attackStat = 6,
            defenseStat = 8,
            luckStat = 5,
            description = "Personaje equilibrado, ideal para resistir ataques y causar daño constante.",
            attacks = listOf(1),
            imageResId = R.drawable.hero_kael
        ),
        Hero(
            id = 2,
            name = "Luna",
            level = 1,
            currentXP = 0,
            role = "Hechicera de datos",
            HpStat = 100,
            attackStat = 11,
            defenseStat = 5,
            luckStat = 7,
            description = "Personaje rápido y ofensivo. Tiene menos vida, pero sus ataques son más fuertes.",
            attacks = listOf(7),
            imageResId = R.drawable.hero_luna
        ),
        Hero(
            id = 3,
            name = "Rex",
            level = 1,
            currentXP = 0,
            role = "Tanque de la arena",
            HpStat = 150,
            attackStat = 6,
            defenseStat = 10,
            luckStat = 4,
            description = "Personaje defensivo. Tiene mucha vida, aunque su daño es más bajo.",
            attacks = listOf(3),
            imageResId = R.drawable.hero_rex
        )
    )
}

    fun GetEnemies(): List<Enemy>{

         return listOf(
            Enemy(
                id = 1,
                level = 1,
                name = "Zombie",
                role = "Enemigo básico",
                rewardXp = 20,
                HpStat = 10,
                attackStat = 4,
                defenseStat = 3,
                luckStat = 2,
                description = "No es muy fuerte, pero puede ser molesto en grupo.",
                attacks = listOf(1),
                imageResId = R.drawable.zombie1
            ),
            Enemy(
                id = 2,
                level = 1,
                name = "Slime",
                rewardXp = 10,
                role = "Enemigo Basico",
                HpStat = 50,
                attackStat = 2,
                defenseStat = 8,
                luckStat = 1,
                description = "Es más debil que el zombie, pero también mas rapido.",
                attacks = listOf(3),
                imageResId = R.drawable.slime1
            ),
            Enemy(
                id = 3,
                level = 1,
                name = "Esqueleto",
                rewardXp = 15,
                role = "Enemigo basico",
                HpStat = 120,
                attackStat = 11,
                defenseStat = 2,
                luckStat = 1,
                description = "Es un enemigo más fuerte, con mala defensa y ataques decentes.",
                attacks = listOf(2),
                imageResId = R.drawable.skeleton1
            )

         )
     }

