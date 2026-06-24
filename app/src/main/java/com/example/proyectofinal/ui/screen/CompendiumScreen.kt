package com.example.proyectofinal.ui.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.resources.GetEnemies
import com.example.proyectofinal.data.resources.getPlayableCharacters
import com.example.proyectofinal.ui.components.EnemyPortraitFrame
import com.example.proyectofinal.ui.components.HeroPortraitFrame

@Composable
fun CompendiumScreen(
    onBack: () -> Unit
) {
    val heroes = getPlayableCharacters()
    val enemies = GetEnemies()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Compendio",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Consulta heroes y enemigos disponibles",
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            Text(
                text = "Heroes disponibles",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0BA896)
            )
        }

        items(heroes, key = { "hero_${it.id}" }) { hero ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeroPortraitFrame(
                        modifier = Modifier.size(88.dp),
                        imageResId = hero.imageResId,
                        characterName = hero.name
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(hero.name, fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
                        Text(hero.role, color = Color(0xFF5F5F7A))
                        Text("HP ${hero.HpStat} • ATQ ${hero.attackStat} • DEF ${hero.defenseStat}")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Enemigos disponibles",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE63946)
            )
        }

        items(enemies, key = { "enemy_${it.id}" }) { enemy ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnemyPortraitFrame(
                        modifier = Modifier.size(88.dp),
                        imageResId = enemy.imageResId,
                        characterName = enemy.name
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(enemy.name, fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
                        Text(enemy.role, color = Color(0xFF5F5F7A))
                        Text("HP ${enemy.HpStat} • ATQ ${enemy.attackStat} • DEF ${enemy.defenseStat}")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
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

