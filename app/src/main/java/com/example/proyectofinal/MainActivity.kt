package com.example.proyectofinal

import androidx.compose.foundation.layout.*
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleIoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BattleIoApp()
                }
            }
        }
    }
}

@Composable
fun BattleIoTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Color(0xFF6C63FF),
        secondary = Color(0xFF00A896),
        tertiary = Color(0xFFFFB703),
        background = Color(0xFFF7F4FF),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1F1F2E)
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

data class GamePack(
    val id: Int,
    val nameKey: String,
    val priceUsd: Int,
    val coins: Int,
    val bonusKey: String
)

data class MatchHistory(
    val id: Int,
    val titleKey: String,
    val resultKey: String,
    val score: String
)

data class AttackMove(
    val name: String,
    val damage: Int,
    val description: String
)

data class BattleCharacter(
    val id: Int,
    val name: String,
    val role: String,
    val maxHp: Int,
    val attackBonus: Int,
    val description: String,
    val attacks: List<AttackMove>
)

data class StoryChapter(
    val id: Int,
    val title: String,
    val description: String,
    val enemy: BattleCharacter,
    val rewardCoins: Int,
    val rewardXp: Int
)

@Composable
fun BattleIoApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("battle_io_data", Context.MODE_PRIVATE) }
    val savedPackId = prefs.getInt("selectedPackId", -1)

    var selectedNav by rememberSaveable { mutableStateOf(prefs.getString("selectedNav", "inicio") ?: "inicio") }
    var nombre by rememberSaveable { mutableStateOf(prefs.getString("nombre", "Jonathan Rivera") ?: "Jonathan Rivera") }
    var correo by rememberSaveable { mutableStateOf(prefs.getString("correo", "jonathan@uam.edu.ni") ?: "jonathan@uam.edu.ni") }
    var bio by rememberSaveable {
        mutableStateOf(
            prefs.getString(
                "bio",
                "Jugador competitivo con interés en mejorar su rendimiento."
            ) ?: "Jugador competitivo con interés en mejorar su rendimiento."
        )
    }
    var profileImageUri by rememberSaveable { mutableStateOf(prefs.getString("profileImageUri", null)) }
    var nivel by rememberSaveable { mutableIntStateOf(prefs.getInt("nivel", 8)) }
    var monedas by rememberSaveable { mutableIntStateOf(prefs.getInt("monedas", 1250)) }
    var partidasGanadas by rememberSaveable { mutableIntStateOf(prefs.getInt("partidasGanadas", 12)) }
    var partidasJugadas by rememberSaveable { mutableIntStateOf(prefs.getInt("partidasJugadas", 20)) }
    var selectedPackId by rememberSaveable { mutableStateOf(if (savedPackId == -1) null else savedPackId) }
    var graphicsQuality by rememberSaveable { mutableStateOf(normalizeQuality(prefs.getString("graphicsQuality", "high"))) }
    var selectedLanguage by rememberSaveable { mutableStateOf(normalizeLanguage(prefs.getString("selectedLanguage", "es"))) }
    var musicVolume by rememberSaveable { mutableFloatStateOf(prefs.getFloat("musicVolume", 75f)) }
    var soundVolume by rememberSaveable { mutableFloatStateOf(prefs.getFloat("soundVolume", 80f)) }

    var selectedCharacterId by rememberSaveable { mutableIntStateOf(prefs.getInt("selectedCharacterId", 1)) }
    var storyProgress by rememberSaveable { mutableIntStateOf(prefs.getInt("storyProgress", 1).coerceIn(1, 3)) }
    var playerHp by rememberSaveable { mutableIntStateOf(100) }
    var enemyHp by rememberSaveable { mutableIntStateOf(100) }
    var battleMessage by rememberSaveable { mutableStateOf("El combate está por comenzar.") }
    var battleFinished by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(
        selectedNav,
        nombre,
        correo,
        bio,
        profileImageUri,
        nivel,
        monedas,
        partidasGanadas,
        partidasJugadas,
        selectedPackId,
        graphicsQuality,
        selectedLanguage,
        musicVolume,
        soundVolume,
        selectedCharacterId,
        storyProgress
    ) {
        prefs.edit()
            .putString("selectedNav", selectedNav)
            .putString("nombre", nombre)
            .putString("correo", correo)
            .putString("bio", bio)
            .putString("profileImageUri", profileImageUri)
            .putInt("nivel", nivel)
            .putInt("monedas", monedas)
            .putInt("partidasGanadas", partidasGanadas)
            .putInt("partidasJugadas", partidasJugadas)
            .putInt("selectedPackId", selectedPackId ?: -1)
            .putString("graphicsQuality", graphicsQuality)
            .putString("selectedLanguage", selectedLanguage)
            .putFloat("musicVolume", musicVolume)
            .putFloat("soundVolume", soundVolume)
            .putInt("selectedCharacterId", selectedCharacterId)
            .putInt("storyProgress", storyProgress)
            .apply()
    }

    val packs = remember {
        listOf(
            GamePack(1, "pack_initial", 10, 1000, "bonus_initial"),
            GamePack(2, "pack_pro", 20, 2300, "bonus_pro"),
            GamePack(3, "pack_legendary", 30, 3800, "bonus_legendary")
        )
    }

    val matches = remember(partidasGanadas, partidasJugadas) {
        listOf(
            MatchHistory(1, "match_quick", "result_win", "+120 XP"),
            MatchHistory(2, "match_survival", "result_loss", "+40 XP"),
            MatchHistory(3, "match_competitive", "result_win", "+180 XP"),
            MatchHistory(4, "match_weekly", "result_win", "+250 XP")
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedNav == "inicio",
                    onClick = { selectedNav = "inicio" },
                    icon = { Text("🏠") },
                    label = { Text(t(selectedLanguage, "nav_home")) }
                )
                NavigationBarItem(
                    selected = selectedNav == "partidas" || selectedNav == "historia" || selectedNav == "seleccion_personaje" || selectedNav == "combate",
                    onClick = { selectedNav = "partidas" },
                    icon = { Text("⚔️") },
                    label = { Text(t(selectedLanguage, "nav_matches")) }
                )
                NavigationBarItem(
                    selected = selectedNav == "tienda",
                    onClick = { selectedNav = "tienda" },
                    icon = { Text("🛒") },
                    label = { Text(t(selectedLanguage, "nav_store")) }
                )
                NavigationBarItem(
                    selected = selectedNav == "perfil",
                    onClick = { selectedNav = "perfil" },
                    icon = { Text("👤") },
                    label = { Text(t(selectedLanguage, "nav_profile")) }
                )
                NavigationBarItem(
                    selected = selectedNav == "config",
                    onClick = { selectedNav = "config" },
                    icon = { Text("⚙️") },
                    label = { Text(t(selectedLanguage, "nav_settings")) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFEDE9FE),
                            Color(0xFFF8F7FF),
                            Color.White
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            when (selectedNav) {
                "inicio" -> HomeScreen(
                    language = selectedLanguage,
                    nombre = nombre,
                    profileImageUri = profileImageUri,
                    nivel = nivel,
                    monedas = monedas,
                    partidasGanadas = partidasGanadas,
                    partidasJugadas = partidasJugadas,
                    storyProgress = storyProgress,
                    onGoToStore = { selectedNav = "tienda" },
                    onGoToProfile = { selectedNav = "perfil" },
                    onGoToStory = { selectedNav = "historia" }
                )

                "partidas" -> MatchesScreen(
                    language = selectedLanguage,
                    matches = matches,
                    partidasGanadas = partidasGanadas,
                    partidasJugadas = partidasJugadas,
                    storyProgress = storyProgress,
                    onPlayMatch = {
                        partidasJugadas++
                        partidasGanadas++
                        nivel++
                        monedas += 150
                        scope.launch {
                            snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_match_won"))
                        }
                    },
                    onGoToStory = { selectedNav = "historia" }
                )

                "historia" -> StoryScreen(
                    storyProgress = storyProgress,
                    onStart = { selectedNav = "seleccion_personaje" },
                    onBack = { selectedNav = "partidas" },
                    onResetStory = {
                        storyProgress = 1
                        scope.launch { snackbarHostState.showSnackbar("Historia reiniciada") }
                    }
                )

                "seleccion_personaje" -> CharacterSelectionScreen(
                    characters = getPlayableCharacters(),
                    selectedCharacterId = selectedCharacterId,
                    onSelectCharacter = { selectedCharacterId = it },
                    onStartBattle = {
                        val player = getPlayableCharacters().first { it.id == selectedCharacterId }
                        val chapter = getStoryChapters().first { it.id == storyProgress }
                        playerHp = player.maxHp
                        enemyHp = chapter.enemy.maxHp
                        battleMessage = "${chapter.enemy.name} apareció en la arena. Es tu turno."
                        battleFinished = false
                        selectedNav = "combate"
                    },
                    onBack = { selectedNav = "historia" }
                )

                "combate" -> {
                    val player = getPlayableCharacters().first { it.id == selectedCharacterId }
                    val chapter = getStoryChapters().first { it.id == storyProgress }
                    val enemy = chapter.enemy

                    BattleScreen(
                        player = player,
                        enemy = enemy,
                        chapter = chapter,
                        playerHp = playerHp,
                        enemyHp = enemyHp,
                        battleMessage = battleMessage,
                        battleFinished = battleFinished,
                        onAttack = { attack ->
                            if (!battleFinished) {
                                val playerDamage = attack.damage + player.attackBonus + (nivel / 3)
                                val updatedEnemyHp = (enemyHp - playerDamage).coerceAtLeast(0)
                                enemyHp = updatedEnemyHp

                                if (updatedEnemyHp == 0) {
                                    battleMessage = "¡Victoria! ${player.name} usó ${attack.name} y derrotó a ${enemy.name}. Recuperaste un fragmento del núcleo."
                                    battleFinished = true
                                    partidasJugadas++
                                    partidasGanadas++
                                    nivel++
                                    monedas += chapter.rewardCoins
                                    storyProgress = (storyProgress + 1).coerceAtMost(3)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Ganaste ${chapter.rewardCoins} monedas y ${chapter.rewardXp} XP")
                                    }
                                } else {
                                    val enemyAttack = enemy.attacks.random()
                                    val enemyDamage = enemyAttack.damage + enemy.attackBonus
                                    val updatedPlayerHp = (playerHp - enemyDamage).coerceAtLeast(0)
                                    playerHp = updatedPlayerHp

                                    battleMessage = "${player.name} usó ${attack.name} e hizo $playerDamage de daño. ${enemy.name} respondió con ${enemyAttack.name} e hizo $enemyDamage de daño."

                                    if (updatedPlayerHp == 0) {
                                        battleMessage = "Derrota. ${enemy.name} resistió el ataque y corrompió temporalmente el núcleo. Puedes volver a intentarlo."
                                        battleFinished = true
                                        partidasJugadas++
                                    }
                                }
                            }
                        },
                        onExit = { selectedNav = "partidas" },
                        onRetry = {
                            playerHp = player.maxHp
                            enemyHp = enemy.maxHp
                            battleMessage = "Nuevo intento contra ${enemy.name}. Es tu turno."
                            battleFinished = false
                        }
                    )
                }

                "tienda" -> StoreScreen(
                    language = selectedLanguage,
                    packs = packs,
                    selectedPackId = selectedPackId,
                    monedas = monedas,
                    onSelectPack = { selectedPackId = it },
                    onPurchase = {
                        val selectedPack = packs.firstOrNull { it.id == selectedPackId }
                        scope.launch {
                            if (selectedPack == null) {
                                snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_select_pack"))
                            } else {
                                monedas += selectedPack.coins
                                snackbarHostState.showSnackbar(
                                    "${t(selectedLanguage, "snackbar_purchase")}: ${t(selectedLanguage, selectedPack.nameKey)}"
                                )
                            }
                        }
                    }
                )

                "perfil" -> ProfileScreen(
                    language = selectedLanguage,
                    nombre = nombre,
                    correo = correo,
                    bio = bio,
                    profileImageUri = profileImageUri,
                    onNombreChange = { nombre = it },
                    onCorreoChange = { correo = it },
                    onBioChange = { bio = it },
                    onProfileImageChange = { profileImageUri = it },
                    onSave = {
                        scope.launch {
                            snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_profile_saved"))
                        }
                    },
                    onLogout = {
                        scope.launch {
                            snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_logout"))
                        }
                    }
                )

                "config" -> SettingsScreen(
                    language = selectedLanguage,
                    graphicsQuality = graphicsQuality,
                    selectedLanguage = selectedLanguage,
                    musicVolume = musicVolume,
                    soundVolume = soundVolume,
                    onGraphicsQualityChange = { graphicsQuality = it },
                    onLanguageChange = { selectedLanguage = it },
                    onMusicVolumeChange = { musicVolume = it },
                    onSoundVolumeChange = { soundVolume = it }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    language: String,
    nombre: String,
    profileImageUri: String?,
    nivel: Int,
    monedas: Int,
    partidasGanadas: Int,
    partidasJugadas: Int,
    storyProgress: Int,
    onGoToStore: () -> Unit,
    onGoToProfile: () -> Unit,
    onGoToStory: () -> Unit
) {
    val imageBitmap = rememberImageBitmap(profileImageUri)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Battle.io",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Fragmentos del Núcleo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF6C63FF), Color(0xFFFFB3C6))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = t(language, "profile_photo"),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("🎮", style = MaterialTheme.typography.headlineLarge)
                        }
                    }

                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Nivel $nivel • $monedas monedas",
                        color = Color(0xFF5F5F7A)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = t(language, "victories"),
                    value = partidasGanadas.toString(),
                    subtitle = t(language, "won_sub"),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = t(language, "total"),
                    value = partidasJugadas.toString(),
                    subtitle = t(language, "played_sub"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            StoryProgressCard(storyProgress = storyProgress, onGoToStory = onGoToStory)
        }

        item {
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
                    Text(
                        text = t(language, "quick_actions"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Button(
                        onClick = onGoToStory,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Jugar modo historia")
                    }
                    OutlinedButton(
                        onClick = onGoToStore,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "go_store"))
                    }
                    OutlinedButton(
                        onClick = onGoToProfile,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "edit_profile"))
                    }
                }
            }
        }
    }
}

@Composable
fun MatchesScreen(
    language: String,
    matches: List<MatchHistory>,
    partidasGanadas: Int,
    partidasJugadas: Int,
    storyProgress: Int,
    onPlayMatch: () -> Unit,
    onGoToStory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = t(language, "matches_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = t(language, "matches_description"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = t(language, "victories"),
                    value = partidasGanadas.toString(),
                    subtitle = t(language, "won_sub"),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = t(language, "total"),
                    value = partidasJugadas.toString(),
                    subtitle = t(language, "played_sub"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
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
                    Text(
                        text = "Modo Historia",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = "Capítulo actual: $storyProgress de 3. Entra a la arena, elige tu personaje y pelea por turnos contra los Glitches.",
                        color = Color(0xFF5F5F7A)
                    )
                    Button(
                        onClick = onGoToStory,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text("Entrar al modo historia")
                    }
                }
            }
        }

        item {
            Button(
                onClick = onPlayMatch,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A896))
            ) {
                Text(t(language, "play_simulated"))
            }
        }

        items(matches) { match ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = t(language, match.titleKey),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = t(language, match.resultKey),
                            color = Color(0xFF5F5F7A)
                        )
                    }
                    Text(
                        text = match.score,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00A896)
                    )
                }
            }
        }
    }
}

@Composable
fun StoryScreen(
    storyProgress: Int,
    onStart: () -> Unit,
    onBack: () -> Unit,
    onResetStory: () -> Unit
) {
    val chapters = getStoryChapters()
    val chapter = chapters.firstOrNull { it.id == storyProgress } ?: chapters.last()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Modo Historia",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = "Battle.io: Fragmentos del Núcleo",
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = chapter.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF34344A)
                    )
                    Text(
                        text = "Enemigo: ${chapter.enemy.name}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Recompensa: ${chapter.rewardCoins} monedas • ${chapter.rewardXp} XP",
                        color = Color(0xFF00A896),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Sinopsis general",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )
                    Text(
                        text = "En el mundo digital de Battle.io, una falla del sistema creó enemigos llamados Glitches. Tu misión es entrar a la arena, elegir un campeón y recuperar los fragmentos del núcleo mediante combates por turnos.",
                        color = Color(0xFF34344A)
                    )
                }
            }
        }

        item {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Elegir personaje")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Volver a partidas")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onResetStory,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Reiniciar historia")
            }
        }
    }
}

@Composable
fun CharacterSelectionScreen(
    characters: List<BattleCharacter>,
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
                text = "Cada personaje tiene vida, bonus de ataque y habilidades diferentes.",
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
                } else {
                    null
                },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
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
                        text = "Vida: ${character.maxHp} | Bonus ataque: +${character.attackBonus}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ataques: ${character.attacks.joinToString { it.name }}",
                        color = Color(0xFF5F5F7A)
                    )
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

@Composable
fun BattleScreen(
    player: BattleCharacter,
    enemy: BattleCharacter,
    chapter: StoryChapter,
    playerHp: Int,
    enemyHp: Int,
    battleMessage: String,
    battleFinished: Boolean,
    onAttack: (AttackMove) -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Combate por turnos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
            Text(
                text = chapter.title,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            FighterCard(
                title = "Tu personaje",
                character = player,
                currentHp = playerHp,
                barColor = Color(0xFF00A896)
            )
        }

        item {
            FighterCard(
                title = "Enemigo",
                character = enemy,
                currentHp = enemyHp,
                barColor = Color(0xFFE63946)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Text(
                    text = battleMessage,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        if (!battleFinished) {
            item {
                Text(
                    text = "Elige un ataque:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A0CA3)
                )
            }

            items(player.attacks) { attack ->
                AttackButton(attack = attack, onAttack = { onAttack(attack) })
            }
        } else {
            item {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Intentar otra vez")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Volver a partidas")
                }
            }
        }
    }
}

@Composable
fun StoreScreen(
    language: String,
    packs: List<GamePack>,
    selectedPackId: Int?,
    monedas: Int,
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
                text = "Balance actual: $monedas monedas",
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
                onClick = onPurchase,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "buy_pack"))
            }
        }
    }
}

@Composable
fun ProfileScreen(
    language: String,
    nombre: String,
    correo: String,
    bio: String,
    profileImageUri: String?,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onProfileImageChange: (String?) -> Unit,
    onSave: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val imageBitmap = rememberImageBitmap(profileImageUri)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }
            onProfileImageChange(uri.toString())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = t(language, "profile_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
        }

        item {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6C63FF), Color(0xFFFFB3C6))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = t(language, "profile_photo"),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("👤", style = MaterialTheme.typography.headlineLarge)
                }
            }
        }

        item {
            OutlinedButton(
                onClick = { launcher.launch(arrayOf("image/*")) },
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "change_photo"))
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        label = { Text(t(language, "name")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = correo,
                        onValueChange = onCorreoChange,
                        label = { Text(t(language, "email")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bio,
                        onValueChange = onBioChange,
                        label = { Text(t(language, "bio")) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(t(language, "save_changes"))
                    }
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(t(language, "logout"))
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    language: String,
    graphicsQuality: String,
    selectedLanguage: String,
    musicVolume: Float,
    soundVolume: Float,
    onGraphicsQualityChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onMusicVolumeChange: (Float) -> Unit,
    onSoundVolumeChange: (Float) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = t(language, "settings_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
        }

        item {
            SettingsCard(title = t(language, "language")) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OptionChip("Español", selectedLanguage == "es") { onLanguageChange("es") }
                    OptionChip("English", selectedLanguage == "en") { onLanguageChange("en") }
                }
            }
        }

        item {
            SettingsCard(title = t(language, "graphics_quality")) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OptionChip(t(language, "quality_low"), graphicsQuality == "low") { onGraphicsQualityChange("low") }
                    OptionChip(t(language, "quality_medium"), graphicsQuality == "medium") { onGraphicsQualityChange("medium") }
                    OptionChip(t(language, "quality_high"), graphicsQuality == "high") { onGraphicsQualityChange("high") }
                }
            }
        }

        item {
            SettingsCard(title = t(language, "music_volume")) {
                Text("${musicVolume.roundToInt()}%", fontWeight = FontWeight.Bold)
                Slider(
                    value = musicVolume,
                    onValueChange = onMusicVolumeChange,
                    valueRange = 0f..100f
                )
            }
        }

        item {
            SettingsCard(title = t(language, "sound_volume")) {
                Text("${soundVolume.roundToInt()}%", fontWeight = FontWeight.Bold)
                Slider(
                    value = soundVolume,
                    onValueChange = onSoundVolumeChange,
                    valueRange = 0f..100f
                )
            }
        }
    }
}

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
                text = "Capítulo $storyProgress de 3: recupera los fragmentos del núcleo venciendo enemigos por turnos.",
                color = Color.White.copy(alpha = 0.9f)
            )
            LinearProgressIndicator(
                progress = storyProgress / 3f,
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF3A0CA3))
            ) {
                Text("Continuar historia")
            }
        }
    }
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
                progress = hpPercent.coerceIn(0f, 1f),
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
                val uri = Uri.parse(profileImageUri)
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                bitmap.asImageBitmap()
            } catch (_: Exception) {
                null
            }
        } else {
            null
        }
    }

    return imageBitmap
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
