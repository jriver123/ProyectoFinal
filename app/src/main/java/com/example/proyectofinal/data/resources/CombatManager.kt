package com.example.proyectofinal.data.resources

import kotlin.random.Random

class CombatManager(private val random: Random = Random.Default) {

    fun calcularDaño(
        ataque: AttackMove,
        attackStat: Int,
        defenseStat: Int
    ): Int {
        // multiplicador por ataque del personaje
        val multiplicador = 1.0 + (attackStat * 0.01)

        // daño inicial con multiplicador
        val rawDamage = ataque.basedamage * multiplicador

        // aplicar lowroll / highroll (ejemplo: +-10%)
        val rollFactor = random.nextDouble(0.9, 1.1)
        var damage = rawDamage * rollFactor

        // aplicar defensa escalonada
        val defensaReducida = when {
            defenseStat <= 60 -> defenseStat.toDouble()
            defenseStat <= 80 -> 60.0 + (defenseStat - 60) * 0.5
            else -> 60.0 + (20 * 0.5) + (defenseStat - 80) * 0.2
        }

        damage -= defensaReducida

        // nunca menos de 0
        return damage.coerceAtLeast(0.0).toInt()
    }

    private fun rollHit(ataque: AttackMove): Boolean {
        return random.nextDouble() <= ataque.accuracy
    }

    fun realizarAtaqueJugador(
        ataque: AttackMove,
        atacante: Hero,
        defensor: Enemy
    ): Pair<Int, String> {
        if (!rollHit(ataque)) {
            return Pair(0, "${atacante.name} usa ${ataque.name}, pero falla.")
        }

        val danio = calcularDaño(ataque, atacante.attackStat, defensor.defenseStat)
        val mensaje = "${atacante.name} usa ${ataque.name} y causa $danio de daño"
        return Pair(danio, mensaje)
    }

    fun realizarAtaqueEnemigo(
        ataque: AttackMove,
        atacante: Enemy,
        defensor: Hero
    ): Pair<Int, String> {
        if (!rollHit(ataque)) {
            return Pair(0, "${atacante.name} usa ${ataque.name}, pero falla.")
        }

        val danio = calcularDaño(ataque, atacante.attackStat, defensor.defenseStat)
        val mensaje = "${atacante.name} usa ${ataque.name} y causa $danio de daño"
        return Pair(danio, mensaje)
    }


}
