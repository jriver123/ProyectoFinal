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
fun getPlayableCharacters(): List<BattleCharacter> {
    return listOf(
        BattleCharacter(
            id = 1,
            name = "Kael",
            role = "Guerrero del Núcleo",
            maxHp = 125,
            attackBonus = 6,
            description = "Personaje equilibrado, ideal para resistir ataques y causar daño constante.",
            attacks = listOf(
                AttackMove("Golpe de energía", 20, "Ataque básico con energía digital."),
                AttackMove("Corte del núcleo", 30, "Ataque fuerte contra el enemigo."),
                AttackMove("Impacto defensivo", 16, "Ataque seguro de daño moderado.")
            )
        ),
        BattleCharacter(
            id = 2,
            name = "Luna",
            role = "Hechicera de datos",
            maxHp = 100,
            attackBonus = 11,
            description = "Personaje rápido y ofensivo. Tiene menos vida, pero sus ataques son más fuertes.",
            attacks = listOf(
                AttackMove("Rayo binario", 24, "Descarga mágica de código puro."),
                AttackMove("Pulso de datos", 34, "Ataque poderoso que altera al rival."),
                AttackMove("Chispa digital", 18, "Ataque rápido y preciso.")
            )
        ),
        BattleCharacter(
            id = 3,
            name = "Rex",
            role = "Tanque de la arena",
            maxHp = 150,
            attackBonus = 3,
            description = "Personaje defensivo. Tiene mucha vida, aunque su daño es más bajo.",
            attacks = listOf(
                AttackMove("Puño blindado", 18, "Golpe pesado con armadura digital."),
                AttackMove("Carga frontal", 26, "Ataque físico directo."),
                AttackMove("Contraataque", 20, "Movimiento estable y resistente.")
            )
        )
    )
}

fun getStoryChapters(): List<StoryChapter> {
    val glitchBasic = BattleCharacter(
        id = 101,
        name = "Glitch menor",
        role = "Error corrupto del sistema",
        maxHp = 95,
        attackBonus = 4,
        description = "Criatura digital nacida de una falla menor del núcleo.",
        attacks = listOf(
            AttackMove("Ruido digital", 15, "Ataque inestable de baja potencia."),
            AttackMove("Código roto", 22, "Golpe corrupto contra el jugador."),
            AttackMove("Pantalla azul", 18, "Ataque inesperado del sistema.")
        )
    )

    val glitchAdvanced = BattleCharacter(
        id = 102,
        name = "Glitch avanzado",
        role = "Amenaza adaptativa",
        maxHp = 125,
        attackBonus = 7,
        description = "Enemigo que aprende de los movimientos del jugador.",
        attacks = listOf(
            AttackMove("Error crítico", 24, "Ataque fuerte al sistema del jugador."),
            AttackMove("Fragmento corrupto", 28, "Daño directo con energía oscura."),
            AttackMove("Reinicio forzado", 20, "Ataque rápido de interrupción.")
        )
    )

    val glitchSupreme = BattleCharacter(
        id = 103,
        name = "Glitch Supremo",
        role = "Jefe final del núcleo",
        maxHp = 160,
        attackBonus = 9,
        description = "La forma más peligrosa de la corrupción digital.",
        attacks = listOf(
            AttackMove("Colapso del núcleo", 30, "Ataque devastador de energía corrupta."),
            AttackMove("Tormenta de bugs", 26, "Ataque múltiple contra el jugador."),
            AttackMove("Virus final", 34, "Ataque poderoso del jefe final.")
        )
    )

    return listOf(
        StoryChapter(
            id = 1,
            title = "Capítulo 1: El despertar del núcleo",
            description = "El sistema Battle.io ha sido atacado por criaturas Glitch. Tu misión es entrar a la arena, elegir un campeón y recuperar el primer fragmento del núcleo.",
            enemy = glitchBasic,
            rewardCoins = 250,
            rewardXp = 120
        ),
        StoryChapter(
            id = 2,
            title = "Capítulo 2: La arena corrupta",
            description = "Después de la primera victoria, la arena empieza a cambiar sus reglas. Los enemigos ahora reconocen tus movimientos y atacan con más fuerza.",
            enemy = glitchAdvanced,
            rewardCoins = 400,
            rewardXp = 180
        ),
        StoryChapter(
            id = 3,
            title = "Capítulo 3: El fragmento final",
            description = "El núcleo está cerca de ser restaurado, pero el Glitch Supremo aparece como la última defensa del sistema corrupto.",
            enemy = glitchSupreme,
            rewardCoins = 700,
            rewardXp = 300
        )
    )
}
@Composable
fun FighterCard(title: String, character: BattleCharacter, currentHp: Int, barColor: Color) {
    val hpPercent = if (character.maxHp == 0) 0f else currentHp.toFloat() / character.maxHp.toFloat()

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
                text = "Vida: $currentHp / ${character.maxHp}",
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
            Text(text = "${attack.damage} daño", fontWeight = FontWeight.Bold, color = Color(0xFFE63946))
        }
    }
}