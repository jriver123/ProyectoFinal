package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.data.resources.Hero
import com.example.proyectofinal.data.resources.StoryOpponent
import com.example.proyectofinal.data.resources.StoryScene
import com.example.proyectofinal.data.resources.TranssStorySection


@Composable
fun TranssStoryScreen(
    player: Hero,
    section: TranssStorySection,
    onOptionSelected: (optionIndex: Int, sceneId: Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var currentSceneIndex by remember { mutableIntStateOf(0) }
    // dialogueStep: 0 = turno enemigo  |  1 = turno héroe
    var dialogueStep by remember(currentSceneIndex) { mutableIntStateOf(0) }

    val currentScene = section.scenes.getOrNull(currentSceneIndex)
        ?: section.scenes.last()
    val isLastScene = currentSceneIndex >= section.scenes.lastIndex

    // Indica si la animación de typing del bubble activo terminó
    var typingDone by remember(currentSceneIndex, dialogueStep) { mutableStateOf(false) }

    // Reiniciar al cambiar de sección
    LaunchedEffect(section.chapterId) {
        currentSceneIndex = 0
        dialogueStep = 0
    }

    // Función para avanzar en el diálogo (desde botón o tap en pantalla)
    fun advance() {
        when {
            !typingDone -> {
                // Primer tap: terminar animación de typing al instante
                typingDone = true
            }
            dialogueStep == 0 -> {
                // Enemigo terminó → mostrar héroe
                dialogueStep = 1
            }
            !isLastScene -> {
                // Héroe terminó → siguiente escena
                currentSceneIndex++
            }
            // Si es última escena, los botones finales manejan la acción
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D1A))
            // Tap en cualquier parte de la pantalla avanza el diálogo
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    // Solo avanza si aún no estamos en la pantalla final con opciones
                    if (!(isLastScene && dialogueStep == 1 && currentScene.options.isNotEmpty())) {
                        advance()
                    }
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ── Encabezado + Diálogo ──
            Column {
                Text(
                    text = section.chapterTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBDB2FF),
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                // ── Contrincante ──
                OpponentDialogueRow(
                    opponent = section.opponent,
                    scene = currentScene,
                    isActive = dialogueStep == 0,
                    forceComplete = typingDone && dialogueStep == 0,
                    onTypingDone = { if (dialogueStep == 0) typingDone = true }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Jugador — solo visible en step 1 ──
                if (dialogueStep == 1) {
                    PlayerDialogueRow(
                        player = player,
                        scene = currentScene,
                        forceComplete = typingDone,
                        onTypingDone = { typingDone = true }
                    )
                }
            }

            // ── Botones inferiores ──
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                when {
                    // Última escena, step héroe, con opciones → mostrar opciones
                    isLastScene && dialogueStep == 1 && currentScene.options.isNotEmpty() -> {
                        currentScene.options.take(3).forEachIndexed { index, option ->
                            Button(
                                onClick = { onOptionSelected(index, currentScene.sceneId) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3))
                            ) { Text(option, color = Color.White) }
                        }
                    }
                    // Última escena, step héroe, sin opciones → ir al combate
                    isLastScene && dialogueStep == 1 -> {
                        Button(
                            onClick = onContinue,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A896))
                        ) { Text("⚔  Ir al combate", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                    // Cualquier otro paso → botón continuar
                    else -> {
                        Button(
                            onClick = { advance() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A0CA3))
                        ) {
                            Text(
                                if (!typingDone) "⏩  Saltar" else "▶  Continuar",
                                color = Color.White
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("← Volver", color = Color(0xFFBDB2FF)) }

                SceneProgressIndicator(
                    total = section.scenes.size,
                    current = currentSceneIndex
                )
            }
        }
    }
}

@Composable
fun TransssStoryScreen(
    player: Hero,
    section: TranssStorySection,
    onOptionSelected: (optionIndex: Int, sceneId: Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    // Alias para mantener compatibilidad con el nombre usado en navegación.
    TranssStoryScreen(
        player = player,
        section = section,
        onOptionSelected = onOptionSelected,
        onContinue = onContinue,
        onBack = onBack
    )
}

// ──────────────────────────────────────────────────
//  Fila de diálogo del contrincante (parte superior)
// ──────────────────────────────────────────────────
@Composable
private fun OpponentDialogueRow(
    opponent: StoryOpponent,
    scene: StoryScene,
    isActive: Boolean = true,
    forceComplete: Boolean = false,
    onTypingDone: () -> Unit = {}
) {
    // Cuando no es el turno activo, mostrar con alpha reducido (ya completado)
    val alpha = if (isActive) 1f else 0.55f
    Row(
        modifier = Modifier.fillMaxWidth().alpha(alpha),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        TypingDialogueBubble(
            name = opponent.name,
            text = scene.opponentText,
            bubbleColor = Color(0xFF1A0A0A),
            borderColor = Color(0xFFE63946),
            textColor = Color(0xFFF8F8F8),
            nameColor = Color(0xFFE63946),
            forceComplete = forceComplete,
            onTypingDone = onTypingDone,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        CharacterPortrait(
            name = opponent.name,
            imageResId = opponent.imageResId,
            borderColor = Color(0xFFE63946)
        )
    }
}

// ──────────────────────────────────────────────────
//  Fila de diálogo del jugador (parte inferior)
// ──────────────────────────────────────────────────
@Composable
private fun PlayerDialogueRow(
    player: Hero,
    scene: StoryScene,
    forceComplete: Boolean = false,
    onTypingDone: () -> Unit = {}
) {
    val showPlayer = scene.playerText != "..."
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        CharacterPortrait(
            name = player.name,
            imageResId = player.imageResId,
            borderColor = Color(0xFF0BA896)
        )
        Spacer(modifier = Modifier.width(12.dp))

        if (showPlayer) {
            TypingDialogueBubble(
                name = player.name,
                text = scene.playerText,
                bubbleColor = Color(0xFF050D1A),
                borderColor = Color(0xFF0BA896),
                textColor = Color(0xFFF8F8F8),
                nameColor = Color(0xFF0BA896),
                forceComplete = forceComplete,
                onTypingDone = onTypingDone,
                modifier = Modifier.weight(1f)
            )
        } else {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF050D1A)),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Text(
                    text = "...",
                    color = Color(0xFF555577),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────
//  Retrato de personaje
// ──────────────────────────────────────────────────
@Composable
private fun CharacterPortrait(
    name: String,
    imageResId: Int,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .border(3.dp, borderColor, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Retrato de $name",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// ──────────────────────────────────────────────────
//  Globo de texto con efecto de escritura animado
// ──────────────────────────────────────────────────
@Composable
private fun TypingDialogueBubble(
    name: String,
    text: String,
    bubbleColor: Color,
    borderColor: Color,
    textColor: Color,
    nameColor: Color,
    forceComplete: Boolean = false,
    onTypingDone: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var visibleLength by remember(text) { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        visibleLength = 0
        for (i in 1..text.length) {
            if (forceComplete) {
                visibleLength = text.length
                onTypingDone()
                break
            }
            delay(28L)
            visibleLength = i
        }
        onTypingDone()
    }

    // Si se fuerza completar mientras anima, saltar al final
    LaunchedEffect(forceComplete) {
        if (forceComplete && visibleLength < text.length) {
            visibleLength = text.length
            onTypingDone()
        }
    }

    val visibleText = text.take(visibleLength)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bubbleColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, color = nameColor, fontSize = 13.sp)
            Text(text = visibleText, color = textColor, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

// ──────────────────────────────────────────────────
//  Indicador de avance de escenas (puntos)
// ──────────────────────────────────────────────────
@Composable
private fun SceneProgressIndicator(
    total: Int,
    current: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until total) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (i == current) 10.dp else 7.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (i <= current) Color(0xFF3A0CA3)
                        else Color(0xFF333355)
                    )
            )
        }
    }
}







