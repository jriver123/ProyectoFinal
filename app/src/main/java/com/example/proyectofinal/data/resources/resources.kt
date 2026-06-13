package com.example.proyectofinal.data.resources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.random.Random


fun normalizeLanguage(language: String?): String {
    return when (language) {
        "en" -> "en"
        else -> "es"
    }
}

fun normalizeQuality(quality: String?): String {
    return when (quality) {
        "low", "medium", "high" -> quality
        else -> "high"
    }
}

fun t(language: String, key: String): String {
    val es = mapOf(
        "nav_home" to "Inicio",
        "nav_matches" to "Partidas",
        "nav_store" to "Tienda",
        "nav_profile" to "Perfil",
        "nav_settings" to "Config",
        "profile_photo" to "Foto de perfil",
        "quick_actions" to "Acciones rápidas",
        "go_store" to "Ir a la tienda",
        "edit_profile" to "Editar perfil",
        "victories" to "Victorias",
        "total" to "Total",
        "won_sub" to "ganadas",
        "played_sub" to "jugadas",
        "matches_title" to "Partidas",
        "matches_description" to "Juega partidas rápidas o entra al modo historia para avanzar en Battle.io.",
        "play_simulated" to "Jugar partida rápida",
        "match_quick" to "Partida rápida",
        "match_survival" to "Modo supervivencia",
        "match_competitive" to "Partida competitiva",
        "match_weekly" to "Reto semanal",
        "result_win" to "Victoria",
        "result_loss" to "Derrota",
        "store_title" to "Tienda",
        "pack_initial" to "Paquete inicial",
        "pack_pro" to "Paquete Pro",
        "pack_legendary" to "Paquete legendario",
        "bonus_initial" to "Ideal para comenzar con ventaja.",
        "bonus_pro" to "Más monedas y mejor rendimiento.",
        "bonus_legendary" to "Para jugadores que quieren dominar la arena.",
        "buy_pack" to "Comprar paquete",
        "profile_title" to "Perfil del jugador",
        "change_photo" to "Cambiar foto",
        "name" to "Nombre",
        "email" to "Correo",
        "bio" to "Biografía",
        "save_changes" to "Guardar cambios",
        "logout" to "Cerrar sesión",
        "settings_title" to "Configuración",
        "language" to "Idioma",
        "graphics_quality" to "Calidad gráfica",
        "quality_low" to "Baja",
        "quality_medium" to "Media",
        "quality_high" to "Alta",
        "music_volume" to "Volumen de música",
        "sound_volume" to "Volumen de efectos",
        "snackbar_match_won" to "Partida ganada: +150 monedas y +1 nivel",
        "snackbar_select_pack" to "Selecciona un paquete primero",
        "snackbar_purchase" to "Compra realizada",
        "snackbar_profile_saved" to "Perfil guardado correctamente",
        "snackbar_logout" to "Sesión cerrada de forma simulada"
    )

    val en = mapOf(
        "nav_home" to "Home",
        "nav_matches" to "Matches",
        "nav_store" to "Store",
        "nav_profile" to "Profile",
        "nav_settings" to "Settings",
        "profile_photo" to "Profile photo",
        "quick_actions" to "Quick actions",
        "go_store" to "Go to store",
        "edit_profile" to "Edit profile",
        "victories" to "Victories",
        "total" to "Total",
        "won_sub" to "won",
        "played_sub" to "played",
        "matches_title" to "Matches",
        "matches_description" to "Play quick matches or enter story mode to progress in Battle.io.",
        "play_simulated" to "Play quick match",
        "match_quick" to "Quick match",
        "match_survival" to "Survival mode",
        "match_competitive" to "Competitive match",
        "match_weekly" to "Weekly challenge",
        "result_win" to "Victory",
        "result_loss" to "Defeat",
        "store_title" to "Store",
        "pack_initial" to "Initial pack",
        "pack_pro" to "Pro pack",
        "pack_legendary" to "Legendary pack",
        "bonus_initial" to "Ideal to start with an advantage.",
        "bonus_pro" to "More coins and better progress.",
        "bonus_legendary" to "For players who want to dominate the arena.",
        "buy_pack" to "Buy pack",
        "profile_title" to "Player profile",
        "change_photo" to "Change photo",
        "name" to "Name",
        "email" to "Email",
        "bio" to "Biography",
        "save_changes" to "Save changes",
        "logout" to "Log out",
        "settings_title" to "Settings",
        "language" to "Language",
        "graphics_quality" to "Graphics quality",
        "quality_low" to "Low",
        "quality_medium" to "Medium",
        "quality_high" to "High",
        "music_volume" to "Music volume",
        "sound_volume" to "Sound volume",
        "snackbar_match_won" to "Match won: +150 coins and +1 level",
        "snackbar_select_pack" to "Select a pack first",
        "snackbar_purchase" to "Purchase completed",
        "snackbar_profile_saved" to "Profile saved successfully",
        "snackbar_logout" to "Session closed as simulation"
    )

    return if (language == "en") {
        en[key] ?: es[key] ?: key
    } else {
        es[key] ?: key
    }
}


@Composable
fun FighterCardHero(title: String, character: Hero, currentHp: Int, barColor: Color) {
    val hpPercent = if (character.HpStat == 0) 0f else currentHp.toFloat() / character.HpStat.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = character.role, color = Color(0xFF5F5F7A))
            Text(
                text = "Vida: $currentHp / ${character.HpStat}",
                fontWeight = FontWeight.SemiBold
            )
            LinearProgressIndicator(
                progress = { hpPercent.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = barColor,
                trackColor = Color(0xFFE0E0E0)
            )
        }
    }
}
@Composable
fun FighterCardEnemy(title: String, character: Enemy, currentHp: Int, barColor: Color) {
    val hpPercent = if (character.HpStat == 0) 0f else currentHp.toFloat() / character.HpStat.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = character.role, color = Color(0xFF5F5F7A))
            Text(
                text = "Vida: $currentHp / ${character.HpStat}",
                fontWeight = FontWeight.SemiBold
            )
            LinearProgressIndicator(
                progress = { hpPercent.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = barColor,
                trackColor = Color(0xFFE0E0E0)
            )
        }
    }
}
@Composable
fun AttackButton(attack: AttackMove, onAttack: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAttack),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = attack.name, fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
                Text(text = attack.description, color = Color(0xFF5F5F7A))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "${attack.basedamage} daño",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE63946)
            )
        }
    }
}

    fun calcularDaño(
        ataque: AttackMove,
        attackStat: Int,
        defenseStat: Int
    ): Int {
        // multiplicador por ataque del personaje
        val multiplicador = 1.0 + (attackStat * 0.01)

        // daño inicial con multiplicador
        val rawDamage = ataque.basedamage * multiplicador

        // aplicar lowroll / highroll (ejemplo: ±10%)
        val rollFactor = Random.nextDouble(0.9, 1.1)
        var damage = rawDamage * rollFactor

        // aplicar defensa escalonada
        var defensaReducida = 0.0
        when {
            defenseStat <= 60 -> {
                defensaReducida = defenseStat.toDouble()
            }

            defenseStat <= 80 -> {
                defensaReducida = 60.0 + (defenseStat - 60) * 0.5
            }

            else -> {
                defensaReducida = 60.0 + (20 * 0.5) + (defenseStat - 80) * 0.2
            }
        }

        damage -= defensaReducida

        // nunca menos de 0
        return damage.coerceAtLeast(0.0).toInt()
    }

    fun GetListOfAttacks(): List<AttackMove> {
        return listOf(
            AttackMove(
                id = 1, name = "Golpe", basedamage = 16, accuracy = 0.8, type = "Fisico",
                description = "Ataque básico con tus puños"
            ),
            AttackMove(
                id = 2, name = "Corte", basedamage = 20, accuracy = 0.6, type = "Fisico",
                description = "Ataque Cortante contra el enemigo"
            ),
            AttackMove(
                id = 3, name = "Cabezazo", basedamage = 10, accuracy = 0.9, type = "Fisico",
                description = "Ataque basico con tu cabeza"
            ),
            AttackMove(
                id = 4, name = "Patada", basedamage = 18, accuracy = 0.7, type = "Físico",
                description = "Ataque físico con tus piernas"
            ),
            AttackMove(
                id = 5, name = "Lanzamiento", basedamage = 22, accuracy = 0.5, type = "Físico",
                description = "Ataque físico que lanza al enemigo por los aires"
            ),
            AttackMove(
                id = 6, name = "Rayo de energía", basedamage = 24, accuracy = 0.6, type = "Mágico",
                description = "Descarga mágica de código puro."
            ),
            AttackMove(
                id = 7, name = "Bola de fuego", basedamage = 34, accuracy = 0.4, type = "Mágico",
                description = "Ataque poderoso que altera al rival."
            ),
            AttackMove(
                id = 8, name = "Chispa", basedamage = 18, accuracy = 0.8, type = "Mágico",
                description = "Ataque rápido y preciso."

            )
        )
    }

fun getAttacksByIds(vararg ids: Int): List<AttackMove> {
    val attacks = GetListOfAttacks()
    return ids.map { id -> attacks.find { it.id == id } }
        .filterNotNull()
}

fun getStoryChapters(): List<StoryChapter> {
    val enemies = GetEnemies()
    return listOf(
        StoryChapter(
            id = 1,
            title = "Capítulo 1: ",
            description = "",
            enemy = enemies[0],
            rewardCoins = 100,
            rewardXp = 50
        ),
        StoryChapter(
            id = 2,
            title = "Capítulo 1.1: ",
            description = "",
            enemy = enemies[1],
            rewardCoins = 150,
            rewardXp = 75
        ),
        StoryChapter(
            id = 3,
            title = "Capítulo 3: ",
            description = "",
            enemy = enemies[2],
            rewardCoins = 200,
            rewardXp = 100
        )
    )
}


