package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.AttackMove
import com.example.proyectofinal.data.resources.GetListOfAttacks
import com.example.proyectofinal.data.resources.getAttackSkillArtResId
import com.example.proyectofinal.data.resources.maxTargets

@Composable
fun SkillsScreen(
    onOpenSkill: (Int) -> Unit,
    onBack: () -> Unit
) {
    val attacks = GetListOfAttacks()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Habilidades",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Explora todas las habilidades del juego",
                color = Color(0xFF5F5F7A)
            )
        }

        items(attacks, key = { it.id }) { attack ->
            SkillRowCard(
                attack = attack,
                onClick = { onOpenSkill(attack.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
fun SkillDetailScreen(
    attack: AttackMove,
    onBack: () -> Unit
) {
    val iconRes = getAttackSkillArtResId(attack.id)
    val targetLabel = if (attack.maxTargets() == 0) "Self" else "${attack.maxTargets()} objetivo(s)"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = attack.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3A0CA3)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1EEFF))
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "${attack.name} icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(78.dp)
                    )
                }

                InfoRow(label = "Tipo", value = attack.type)
                InfoRow(label = "Daño base", value = attack.basedamage.toString())
                InfoRow(label = "Precision", value = "${(attack.accuracy * 100).toInt()}%")
                InfoRow(label = "Objetivos", value = targetLabel)
                if (attack.healAmount > 0) {
                    InfoRow(label = "Curacion", value = "+${attack.healAmount} HP")
                }
                if (attack.shieldPercent > 0.0) {
                    InfoRow(
                        label = "Escudo",
                        value = "-${(attack.shieldPercent * 100).toInt()}% daño"
                    )
                }
                Text(
                    text = attack.description,
                    color = Color(0xFF5F5F7A)
                )
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Volver")
        }
    }
}

@Composable
private fun SkillRowCard(
    attack: AttackMove,
    onClick: () -> Unit
) {
    val iconRes = getAttackSkillArtResId(attack.id)
    val targetChip = if (attack.maxTargets() == 0) "SELF" else "x${attack.maxTargets()}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1EEFF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "${attack.name} icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = attack.name,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A0CA3)
                )
                Text(
                    text = "${attack.type} • ${attack.basedamage} DMG",
                    color = Color(0xFF5F5F7A)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFEDE7FF))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = targetChip,
                    color = Color(0xFF3A0CA3),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label:",
            modifier = Modifier.width(96.dp),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3A0CA3)
        )
        Text(text = value)
    }
}

