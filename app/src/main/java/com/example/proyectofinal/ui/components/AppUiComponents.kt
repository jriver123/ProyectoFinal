package com.example.proyectofinal.ui.components

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import com.example.proyectofinal.data.resources.necesitaObjetivo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.proyectofinal.R
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.getAttackSkillArtResId
import com.example.proyectofinal.data.resources.maxTargets

@Composable
fun StatCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, color = Color(0xFF5F5F7A))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(text = subtitle, color = Color(0xFF5F5F7A))
        }
    }
}

@Composable
fun StoryProgressCard(storyProgress: Int, onGoToStory: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A0CA3)),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Historia principal",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Capitulo $storyProgress de 3",
                color = Color.White.copy(alpha = 0.9f)
            )
            LinearProgressIndicator(
                progress = { storyProgress / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50.dp)),
                color = Color(0xFFFFB703),
                trackColor = Color.White.copy(alpha = 0.25f)
            )
            Button(
                onClick = onGoToStory,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF3A0CA3)
                )
            ) {
                Text("Continuar historia")
            }
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
            content()
        }
    }
}

@Composable
fun OptionChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) MaterialTheme.colorScheme.primary else Color(0xFFF1F0FF)
    val contentColor = if (selected) Color.White else Color(0xFF3A0CA3)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = contentColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun rememberImageBitmap(profileImageUri: String?): ImageBitmap? {
    val context = LocalContext.current
    var imageBitmap by remember(profileImageUri) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(profileImageUri) {
        imageBitmap = if (profileImageUri != null) {
            try {
                val uri = profileImageUri.toUri()
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                }
                bitmap?.asImageBitmap()
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    return imageBitmap
}

@Composable
fun HeroPortraitFrame(
    modifier: Modifier = Modifier,
    imageResId: Int = 0,
    characterName: String = "Hero"
) {
    Box(
        modifier = modifier
            .size(180.dp)
            .border(
                width = 4.dp,
                color = Color(0xFF0BA896),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF0F9F8))
    ) {
        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "$characterName portrait",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder cuando no hay imagen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = characterName,
                    color = Color(0xFF5F5F7A),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun EnemyPortraitFrame(
    modifier: Modifier = Modifier,
    imageResId: Int = 0,
    characterName: String = "Enemy"
) {
    Box(
        modifier = modifier
            .size(180.dp)
            .border(
                width = 4.dp,
                color = Color(0xFFE63946),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFBF0F0))
    ) {
        if (imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "$characterName portrait",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder cuando no hay imagen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = characterName,
                    color = Color(0xFF5F5F7A),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun AttackCommandBar(
    attacks: List<AttackMove>,
    canUseAttacks: Boolean,
    selectedAttack: AttackMove?,
    selectedTargetsCount: Int,
    onAttackClick: (AttackMove) -> Unit,
    modifier: Modifier = Modifier
) {
    val attackSlots = List(4) { attacks.getOrNull(it) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = when {
                    selectedAttack == null -> "Elige un ataque primero."
                    !selectedAttack.necesitaObjetivo() -> "Ataque de soporte/defensa listo para ejecutar."
                    else -> {
                        val needed = selectedAttack.maxTargets()
                        "Objetivos: $selectedTargetsCount/$needed"
                    }
                },
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF3A0CA3)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                attackSlots.forEach { attack ->
                    val artResId = attack?.let { getAttackSkillArtResId(it.id) } ?: R.drawable.sa_null
                    val enabled = attack != null && canUseAttacks
                    val isSelected = attack != null && selectedAttack?.id == attack.id

                    OutlinedButton(
                        onClick = { if (attack != null) onAttackClick(attack) },
                        enabled = enabled,
                        modifier = Modifier.width(76.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) Color(0xFFEDE7FF) else Color.Transparent,
                            contentColor = Color(0xFF3A0CA3)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = artResId),
                                contentDescription = attack?.name ?: "Ataque vacio",
                                modifier = Modifier.size(34.dp),
                                contentScale = ContentScale.Crop
                            )
                            if (attack != null) {
                                Text(
                                    text = if (attack.necesitaObjetivo()) "x${attack.maxTargets()}" else "SELF",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

