package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.GamePack
import com.example.proyectofinal.data.resources.HeroUnlockOffer
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.navigation.AppRoutes


@Composable
fun StoreScreen(
    usuario: UsuarioUI,
    language: String,
    packs: List<GamePack>,
    heroOffers: List<HeroUnlockOffer>,
    unlockedHeroIds: Set<Int>,
    selectedPackId: Int?,
    navController: NavController,
    onSelectPack: (Int) -> Unit,
    onPurchase: () -> Unit,
    onBuyHero: (HeroUnlockOffer) -> Unit
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

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Heroes desbloqueables",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Kael viene desbloqueado por defecto. Luna y Rex cuestan 2000 monedas.",
                color = Color(0xFF5F5F7A)
            )
        }

        items(heroOffers, key = { "offer_${it.heroId}" }) { offer ->
            val unlocked = offer.heroId in unlockedHeroIds
            val canBuy = !unlocked && usuario.monedas >= offer.priceCoins
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(72.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (offer.heroImageResId != 0) {
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = offer.heroImageResId),
                                    contentDescription = offer.heroName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(72.dp)
                                )
                            } else {
                                Text(offer.heroName.take(2), fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(offer.heroName, fontWeight = FontWeight.Bold, color = Color(0xFF3A0CA3))
                            Text(offer.heroRole, color = Color(0xFF5F5F7A))
                            Text("${offer.priceCoins} monedas", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (unlocked) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Comprado")
                        }
                    } else {
                        Button(
                            onClick = { onBuyHero(offer) },
                            enabled = canBuy,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(if (canBuy) "Comprar heroe" else "Monedas insuficientes")
                        }
                    }
                }
            }
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
