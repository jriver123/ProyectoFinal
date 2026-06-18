package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
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

// ──────────────────────────────────────────────────
//  Pantalla principal de transición de historia
// ──────────────────────────────────────────────────
@Composable
fun TranssStoryScreen(
    player: Hero,
    section: TranssStorySection,
    onOptionSelected: (optionIndex: Int, sceneId: Int) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    var currentSceneIndex by remember { mutableIntStateOf(0) }
    val currentScene = section.scenes.getOrNull(currentSceneIndex)
        ?: section.scenes.last()

    val isLastScene = currentSceneIndex >= section.scenes.lastIndex

    // Reiniciar al cambiar de sección (p.ej. al entrar de nuevo)
    LaunchedEffect(section.chapterId) {
        currentSceneIndex = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // ── Encabezado ──
        Column {
            Text(
                text = section.chapterTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBDB2FF),
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // ── Contrincante (parte superior) ──
            OpponentDialogueRow(
                opponent = section.opponent,
                scene = currentScene
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Jugador (parte inferior del diálogo) ──
            PlayerDialogueRow(
                player = player,
                scene = currentScene
            )
        }

        // ── Botones de opciones / continuar ──
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (isLastScene && currentScene.options.isNotEmpty()) {
                // Mostrar opciones solo en la última escena si las tiene
                currentScene.options.take(3).forEachIndexed { index, option ->
                    Button(
                        onClick = { onOptionSelected(index, currentScene.sceneId) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3A0CA3)
                        )
                    ) {
                        Text(option, color = Color.White)
                    }
                }
            } else if (!isLastScene) {
                // Avanzar a la siguiente escena
                Button(
                    onClick = { currentSceneIndex++ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3A0CA3)
                    )
                ) {
                    Text("▶  Continuar", color = Color.White)
                }
            } else {
                // Última escena sin opciones → ir al combate
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00A896)
                    )
                ) {
                    Text("⚔  Ir al combate", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("← Volver", color = Color(0xFFBDB2FF))
            }

            // Indicador de progreso de escenas
            SceneProgressIndicator(
                total = section.scenes.size,
                current = currentSceneIndex
            )
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
    scene: StoryScene
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        // Globo de texto
        TypingDialogueBubble(
            name = opponent.name,
            text = scene.opponentText,
            bubbleColor = Color(0xFF1A0A0A),
            borderColor = Color(0xFFE63946),
            textColor = Color(0xFFF8F8F8),
            nameColor = Color(0xFFE63946),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Retrato
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
    scene: StoryScene
) {
    // Si el texto del jugador es solo "..." no mostramos typing
    val showPlayer = scene.playerText != "..."

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        // Retrato
        CharacterPortrait(
            name = player.name,
            imageResId = player.imageResId,
            borderColor = Color(0xFF0BA896)
        )
        Spacer(modifier = Modifier.width(12.dp))

        if (showPlayer) {
            // Globo de texto con efecto typing
            TypingDialogueBubble(
                name = player.name,
                text = scene.playerText,
                bubbleColor = Color(0xFF050D1A),
                borderColor = Color(0xFF0BA896),
                textColor = Color(0xFFF8F8F8),
                nameColor = Color(0xFF0BA896),
                modifier = Modifier.weight(1f)
            )
        } else {
            // Globo de silencio
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
    modifier: Modifier = Modifier
) {
    // Carácter a carácter usando corrutina — 28 ms por carácter
    var visibleLength by remember(text) { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        visibleLength = 0
        for (i in 1..text.length) {
            delay(28L)
            visibleLength = i
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
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                color = nameColor,
                fontSize = 13.sp
            )
            Text(
                text = visibleText,
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
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







