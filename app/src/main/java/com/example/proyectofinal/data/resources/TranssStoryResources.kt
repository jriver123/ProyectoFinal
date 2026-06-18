package com.example.proyectofinal.data.resources

import com.example.proyectofinal.R

// ──────────────────────────────────────────────────
//  Modelo de contrincante narrativo de historia
//  (diferente a Enemy, que es para el combate)
// ──────────────────────────────────────────────────
data class StoryOpponent(
    val id: Int,
    val name: String,
    val description: String,
    val imageResId: Int
)

// ──────────────────────────────────────────────────
//  Una sola escena de diálogo dentro de un capítulo
// ──────────────────────────────────────────────────
data class StoryScene(
    val sceneId: Int,
    val opponentText: String,        // Lo que dice el contrincante
    val playerText: String,          // Lo que dice el jugador
    val options: List<String> = emptyList()   // Opciones del jugador (vacío = botón "Continuar")
)

// ──────────────────────────────────────────────────
//  Sección completa de un capítulo de transición:
//  título + contrincante + lista ordenada de escenas
// ──────────────────────────────────────────────────
data class TranssStorySection(
    val chapterId: Int,
    val chapterTitle: String,
    val opponent: StoryOpponent,
    val scenes: List<StoryScene>
)

// ──────────────────────────────────────────────────
//  Catálogo de contrincantes de historia
// ──────────────────────────────────────────────────
fun createStoryOpponents(): List<StoryOpponent> {
    return listOf(
        StoryOpponent(
            id = 1,
            name = "Aiden",
            description = "Explorador del sistema central. Analiza a sus rivales antes de atacar.",
            imageResId = R.drawable.hero_rex
        ),
        StoryOpponent(
            id = 2,
            name = "Nyra",
            description = "Combatiente táctica. Fría, rápida y muy segura de sí misma.",
            imageResId = R.drawable.hero_luna
        ),
        StoryOpponent(
            id = 3,
            name = "Drax",
            description = "Veterano de las arenas digitales. Busca medir la fuerza de nuevos retadores.",
            imageResId = R.drawable.hero_kael
        )
    )
}

fun getStoryOpponentByChapter(chapterId: Int): StoryOpponent {
    val opponents = createStoryOpponents()
    return when (chapterId) {
        1    -> opponents[0] // Aiden
        2    -> opponents[1] // Nyra
        3    -> opponents[2] // Drax
        else -> opponents[2]
    }
}

// ──────────────────────────────────────────────────
//  Función principal: devuelve la sección completa
//  de transición según capítulo e índice de escena
// ──────────────────────────────────────────────────
fun GetTranssStorySec(chapterId: Int): TranssStorySection {
    val opponent = getStoryOpponentByChapter(chapterId)

    return when (chapterId) {
        1 -> TranssStorySection(
            chapterId = 1,
            chapterTitle = "Capítulo 1: Primer contacto",
            opponent = opponent,
            scenes = listOf(
                StoryScene(
                    sceneId = 1,
                    opponentText = "Así que tú eres el nuevo combatiente. No pareces listo para esta arena.",
                    playerText = "...",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 2,
                    opponentText = "Esta arena no es un juego. Muchos han llegado aquí pensando lo mismo que tú.",
                    playerText = "Tal vez no me conozcas todavía, pero no pienso retroceder.",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 3,
                    opponentText = "Interesante respuesta. Demuéstramelo entonces.",
                    playerText = "¿Cómo quieres proceder?",
                    options = listOf(
                        "Estoy listo para pelear.",
                        "Solo quiero entender qué está pasando.",
                        "No subestimes a tu rival."
                    )
                )
            )
        )

        2 -> TranssStorySection(
            chapterId = 2,
            chapterTitle = "Capítulo 2: Prueba de voluntad",
            opponent = opponent,
            scenes = listOf(
                StoryScene(
                    sceneId = 1,
                    opponentText = "Has avanzado más de lo esperado. Eso me sorprende.",
                    playerText = "...",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 2,
                    opponentText = "Este punto separa a los valientes de los débiles. ¿Dónde encajas tú?",
                    playerText = "He llegado hasta aquí por una razón. No voy a detenerme ahora.",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 3,
                    opponentText = "Palabras bonitas. Pero las palabras no ganan batallas.",
                    playerText = "¿Qué es lo que realmente buscas aquí?",
                    options = listOf(
                        "Acepto el desafío.",
                        "Quiero saber quién está detrás de todo esto.",
                        "Si quieres detenerme, inténtalo."
                    )
                )
            )
        )

        3 -> TranssStorySection(
            chapterId = 3,
            chapterTitle = "Capítulo 3: El guardián",
            opponent = opponent,
            scenes = listOf(
                StoryScene(
                    sceneId = 1,
                    opponentText = "Muchos llegaron antes que tú. Ninguno logró superar esta fase.",
                    playerText = "...",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 2,
                    opponentText = "Yo soy el guardián de esta arena. Mi trabajo es poner a prueba a quienes llegan.",
                    playerText = "Entonces seré el primero. No vine solo a intentarlo.",
                    options = emptyList()
                ),
                StoryScene(
                    sceneId = 3,
                    opponentText = "Bien. Entonces demuéstramelo. ¿Cómo empezamos?",
                    playerText = "Tú mismo lo pediste.",
                    options = listOf(
                        "Comencemos ya.",
                        "No me intimidas.",
                        "Primero quiero escuchar tu historia."
                    )
                )
            )
        )

        else -> TranssStorySection(
            chapterId = chapterId,
            chapterTitle = "Capítulo $chapterId",
            opponent = opponent,
            scenes = listOf(
                StoryScene(
                    sceneId = 1,
                    opponentText = "Has cambiado el curso de esta historia. Veamos cómo termina.",
                    playerText = "Estoy preparado para lo que venga.",
                    options = listOf(
                        "Seguir adelante.",
                        "Revisar mis decisiones.",
                        "Prepararme para el combate."
                    )
                )
            )
        )
    }
}

