package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.GamePack
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.navigation.AppRoutes


@Composable
fun StoreScreen(
    usuario: UsuarioUI,
    language: String,
    packs: List<GamePack>,
    selectedPackId: Int?,
    navController: NavController,
    onSelectPack: (Int) -> Unit,
    onPurchase: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = t(language, "store_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Balance actual: ${usuario.monedas} monedas",
                color = Color(0xFF5F5F7A)
            )
        }

        items(packs) { pack ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectPack(pack.id) },
                shape = RoundedCornerShape(24.dp),
                border = if (selectedPackId == pack.id) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = t(language, pack.nameKey),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(text = t(language, pack.bonusKey), color = Color(0xFF5F5F7A))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${pack.coins} monedas", fontWeight = FontWeight.Bold)
                        Text("US$ ${pack.priceUsd}", fontWeight = FontWeight.Bold, color = Color(0xFF00A896))
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    onPurchase()
                    navController.navigate(AppRoutes.Home)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "buy_pack"))
            }
        }
    }
}
