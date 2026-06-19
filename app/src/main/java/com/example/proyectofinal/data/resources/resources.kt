package com.example.proyectofinal.data.resources

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image


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
        "login_logo" to "BattleIO",
        "login_title" to "Iniciar Sesión",
        "login_subtitle" to "Bienvenido de nuevo a su espacio seguro.",
        "login_email" to "Correo Electrónico",
        "login_password" to "Contraseña",
        "login_button" to "Entrar",
        "login_forgot" to "¿Olvidaste tu contraseña?",
        "login_register_prompt" to "¿No tienes una cuenta?",
        "login_register_action" to "Regístrate ahora",
        "register_title" to "Crear cuenta",
        "register_username" to "Nombre de usuario",
        "register_description" to "Descripción",
        "register_button" to "Registrarme",
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
        "login_logo" to "BattleIO",
        "login_title" to "Sign In",
        "login_subtitle" to "Welcome back to your secure space.",
        "login_email" to "Email",
        "login_password" to "Password",
        "login_button" to "Enter",
        "login_forgot" to "Forgot your password?",
        "login_register_prompt" to "Don't have an account?",
        "login_register_action" to "Register now",
        "register_title" to "Create account",
        "register_username" to "Username",
        "register_description" to "Description",
        "register_button" to "Register me",
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
fun FighterCardHero(
    title: String,
    character: Hero,
    currentHp: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val hpPercent = if (character.HpStat == 0) 0f else currentHp.toFloat() / character.HpStat.toFloat()
    val xpPercent = if (character.nextLevelXP == 0) 0f else character.currentXP.toFloat() / character.nextLevelXP.toFloat()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .border(
                        width = 3.dp,
                        color = Color(0xFF0BA896),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (character.imageResId != 0) {
                    Image(
                        painter = painterResource(id = character.imageResId),
                        contentDescription = "${character.name} portrait",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Vida: $currentHp / ${character.HpStat}", fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { hpPercent.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50.dp)),
                    color = barColor,
                    trackColor = Color(0xFFE0E0E0)
                )

                Text(text = "XP: ${character.currentXP} / ${character.nextLevelXP}", fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { xpPercent.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50.dp)),
                    color = Color(0xFF00A896),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }
    }
}
@Composable
fun FighterCardEnemy(
    title: String,
    character: Enemy,
    currentHp: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val hpPercent = if (character.HpStat == 0) 0f else currentHp.toFloat() / character.HpStat.toFloat()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .border(
                        width = 3.dp,
                        color = Color(0xFFE63946),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
            ) {
                if (character.imageResId != 0) {
                    Image(
                        painter = painterResource(id = character.imageResId),
                        contentDescription = "${character.name} portrait",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "Vida: $currentHp / ${character.HpStat}", fontWeight = FontWeight.SemiBold)
                LinearProgressIndicator(
                    progress = { hpPercent.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50.dp)),
                    color = barColor,
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }
    }
}


fun levelUP(hero: Hero) {
    hero.level += 1
    hero.nextLevelXP = calcularXpRequerida(hero.level)
    hero.HpStat += 12
    hero.attackStat += 3
    hero.defenseStat += 2
    println("${hero.name} ha subido a nivel ${hero.level}!")
}


fun giveXP(hero: Hero, xpGanado: Int) {
    hero.currentXP += xpGanado
    while (hero.currentXP >= hero.nextLevelXP && hero.level < 40) {
        hero.currentXP -= hero.nextLevelXP
        levelUP(hero)
    }
}
fun enemigoDerrotado(hero: Hero, enemy: Enemy) {
    val xpGanado = enemy.rewardXp
    giveXP(hero, xpGanado)
}
fun calcularXpRequerida(level: Int, xpBase: Int = 100): Int {
    val cappedLevel = level.coerceIn(1, 40)
    return xpBase + ((cappedLevel - 1) * 40)
}

fun getUnlockableAttacksForHero(hero: Hero): List<AttackMove> {
    val allAttacks = GetListOfAttacks()
    val owned = hero.attacks.toSet()
    return allAttacks.filter { it.id !in owned }
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
                id = 3, name = "Cabezazo", basedamage = 15, accuracy = 0.9, type = "Fisico",
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
                id = 7, name = "Bola de fuego", basedamage = 19, accuracy = 0.8, type = "Mágico",
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
            enemies = listOf(enemies[0]),
            rewardCoins = 100,
            rewardXp = 50
        ),
        StoryChapter(
            id = 2,
            title = "Capítulo 1.1: ",
            description = "",
            enemies = listOf(enemies[0]),
            rewardCoins = 150,
            rewardXp = 75
        ),
        StoryChapter(
            id = 3,
            title = "Capítulo 3: ",
            description = "",
            enemies = listOf(enemies[0]),
            rewardCoins = 200,
            rewardXp = 100
        )
    )

}


