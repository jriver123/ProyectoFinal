package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.*
import com.example.proyectofinal.ui.components.HeroPortraitFrame

@Composable
fun CharacterSelectionScreen(
    characters: List<Hero>,
    selectedCharacterId: Int,
    onSelectCharacter: (Int) -> Unit,
    onStartBattle: () -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Elige tu personaje",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Cada personaje tiene nivel, vida, ataque y habilidades diferentes.",
                color = Color(0xFF5F5F7A)
            )
        }

        items(characters) { character ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCharacter(character.id) },
                shape = RoundedCornerShape(24.dp),
                border = if (selectedCharacterId == character.id) {
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                } else null,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    HeroPortraitFrame(
                        modifier = Modifier.size(92.dp),
                        imageResId = character.imageResId,
                        characterName = character.name
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A0CA3)
                        )
                        Text(
                            text = character.role,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00A896)
                        )
                        Text(text = character.description)

                        Text(
                            text = "Nivel: ${character.level}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A0CA3)
                        )
                        Text(
                            text = "Vida: ${character.HpStat} | Ataque: ${character.attackStat}",
                            fontWeight = FontWeight.Bold
                        )

                        val attackMoves = getAttacksByIds(*character.attacks.toIntArray())
                        Text(
                            text = "Ataques: ${attackMoves.joinToString { it.name }}",
                            color = Color(0xFF5F5F7A)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onStartBattle,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Iniciar combate")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Volver a historia")
            }
        }
    }
}
