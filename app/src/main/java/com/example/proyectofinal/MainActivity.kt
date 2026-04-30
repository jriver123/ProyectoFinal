package com.example.proyectofinal

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
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

@Composable
fun BattleIoApp() {
    val context = LocalContext.current

    val prefs = remember {
        context.getSharedPreferences("battle_io_data", Context.MODE_PRIVATE)
    }

    val savedPackId = prefs.getInt("selectedPackId", -1)

    var selectedNav by rememberSaveable {
        mutableStateOf(prefs.getString("selectedNav", "inicio") ?: "inicio")
    }

    var nombre by rememberSaveable {
        mutableStateOf(prefs.getString("nombre", "Jonathan Rivera") ?: "Jonathan Rivera")
    }

    var correo by rememberSaveable {
        mutableStateOf(prefs.getString("correo", "jonathan@uam.edu.ni") ?: "jonathan@uam.edu.ni")
    }

    var bio by rememberSaveable {
        mutableStateOf(
            prefs.getString(
                "bio",
                "Jugador competitivo con interés en mejorar su rendimiento."
            ) ?: "Jugador competitivo con interés en mejorar su rendimiento."
        )
    }

    var profileImageUri by rememberSaveable {
        mutableStateOf(prefs.getString("profileImageUri", null))
    }

    var nivel by rememberSaveable {
        mutableIntStateOf(prefs.getInt("nivel", 8))
    }

    var monedas by rememberSaveable {
        mutableIntStateOf(prefs.getInt("monedas", 1250))
    }

    var partidasGanadas by rememberSaveable {
        mutableIntStateOf(prefs.getInt("partidasGanadas", 12))
    }

    var partidasJugadas by rememberSaveable {
        mutableIntStateOf(prefs.getInt("partidasJugadas", 20))
    }

    var selectedPackId by rememberSaveable {
        mutableStateOf<Int?>(if (savedPackId == -1) null else savedPackId)
    }

    var graphicsQuality by rememberSaveable {
        mutableStateOf(normalizeQuality(prefs.getString("graphicsQuality", "high")))
    }

    var selectedLanguage by rememberSaveable {
        mutableStateOf(normalizeLanguage(prefs.getString("selectedLanguage", "es")))
    }

    var musicVolume by rememberSaveable {
        mutableFloatStateOf(prefs.getFloat("musicVolume", 75f))
    }

    var soundVolume by rememberSaveable {
        mutableFloatStateOf(prefs.getFloat("soundVolume", 80f))
    }

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
        soundVolume
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
                    selected = selectedNav == "partidas",
                    onClick = { selectedNav = "partidas" },
                    icon = { Text("🎮") },
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
                    onGoToStore = { selectedNav = "tienda" },
                    onGoToProfile = { selectedNav = "perfil" }
                )

                "partidas" -> MatchesScreen(
                    language = selectedLanguage,
                    matches = matches,
                    partidasGanadas = partidasGanadas,
                    partidasJugadas = partidasJugadas,
                    onPlayMatch = {
                        partidasJugadas++
                        partidasGanadas++
                        nivel++
                        monedas += 150

                        scope.launch {
                            snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_match_won"))
                        }
                    }
                )

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
                                    "${t(selectedLanguage, "snackbar_purchase")}: ${
                                        t(selectedLanguage, selectedPack.nameKey)
                                    }. ${t(selectedLanguage, "snackbar_added")} ${selectedPack.coins} ${
                                        t(selectedLanguage, "coins_lower")
                                    }."
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
                    nivel = nivel,
                    monedas = monedas,
                    partidasGanadas = partidasGanadas,
                    partidasJugadas = partidasJugadas,
                    onNombreChange = { nombre = it },
                    onCorreoChange = { correo = it },
                    onBioChange = { bio = it },
                    onProfileImageUriChange = { profileImageUri = it },
                    onSave = {
                        scope.launch {
                            if (nombre.isBlank() || correo.isBlank() || bio.isBlank()) {
                                snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_complete_profile"))
                            } else {
                                snackbarHostState.showSnackbar(t(selectedLanguage, "snackbar_profile_saved"))
                            }
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
    onGoToStore: () -> Unit,
    onGoToProfile: () -> Unit
) {
    val context = LocalContext.current

    var homeImageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(profileImageUri) {
        homeImageBitmap = if (profileImageUri != null) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF6C63FF),
                                        Color(0xFFFFB3C6)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (homeImageBitmap != null) {
                            Image(
                                bitmap = homeImageBitmap!!,
                                contentDescription = t(language, "profile_photo"),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = obtenerIniciales(nombre),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${t(language, "welcome")}, $nombre",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${t(language, "level")} $nivel • $monedas ${t(language, "coins_lower")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6D6875)
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
                    title = t(language, "won"),
                    value = partidasGanadas.toString(),
                    subtitle = t(language, "matches"),
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = t(language, "played"),
                    value = partidasJugadas.toString(),
                    subtitle = t(language, "total"),
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
                        text = t(language, "quick_actions"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )

                    Button(
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
    onPlayMatch: () -> Unit
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
            Button(
                onClick = onPlayMatch,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = t(language, match.resultKey),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (match.resultKey == "result_win") Color(0xFF00875A) else Color(0xFFD00000)
                        )
                    }

                    Text(
                        text = match.score,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C63FF)
                    )
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
    val selectedPack = packs.firstOrNull { it.id == selectedPackId }

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
                text = t(language, "store_description"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = t(language, "current_balance"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )

                    Text(
                        text = "$monedas ${t(language, "available_coins")}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        items(packs) { pack ->
            PackCard(
                language = language,
                pack = pack,
                isSelected = selectedPackId == pack.id,
                onSelect = { onSelectPack(pack.id) }
            )
        }

        item {
            PurchaseSummary(language = language, selectedPack = selectedPack)
        }

        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onPurchase,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "buy_now"))
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
    nivel: Int,
    monedas: Int,
    partidasGanadas: Int,
    partidasJugadas: Int,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onProfileImageUriChange: (String?) -> Unit,
    onSave: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    var profileImageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }

            onProfileImageUriChange(uri.toString())
        }
    }

    LaunchedEffect(profileImageUri) {
        profileImageBitmap = if (profileImageUri != null) {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = t(language, "profile_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )

            Text(
                text = t(language, "profile_description"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF6C63FF),
                                        Color(0xFF00A896)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageBitmap != null) {
                            Image(
                                bitmap = profileImageBitmap!!,
                                contentDescription = t(language, "profile_photo"),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = obtenerIniciales(nombre),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            imagePickerLauncher.launch(arrayOf("image/*"))
                        },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "choose_profile_photo"))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (nombre.isBlank()) t(language, "player_without_name") else nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = correo.ifBlank { t(language, "email_not_registered") },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6D6875)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = t(language, "level"),
                            value = nivel.toString(),
                            subtitle = t(language, "current"),
                            modifier = Modifier.weight(1f)
                        )

                        StatCard(
                            title = t(language, "coins"),
                            value = monedas.toString(),
                            subtitle = t(language, "balance"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = t(language, "edit_info"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        label = { Text(t(language, "name")) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = onCorreoChange,
                        label = { Text(t(language, "email")) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = onBioChange,
                        label = { Text(t(language, "bio")) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(t(language, "save_profile"))
                    }

                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD00000)
                        )
                    ) {
                        Text(t(language, "logout"))
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F0FF))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = t(language, "player_summary"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A0CA3)
                    )

                    Divider(color = Color(0xFFD8D3FF))

                    Text("${t(language, "matches_played")}: $partidasJugadas")
                    Text("${t(language, "matches_won")}: $partidasGanadas")
                    Text("${t(language, "performance")}: ${calcularRendimiento(partidasGanadas, partidasJugadas)}%")
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = t(language, "settings_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )

            Text(
                text = t(language, "settings_description"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5F5F7A)
            )
        }

        item {
            SettingCard(
                title = t(language, "graphics"),
                subtitle = "${t(language, "current_quality")}: ${qualityLabel(language, graphicsQuality)}"
            ) {
                OptionsBar(
                    options = listOf("low", "medium", "high", "ultra"),
                    selectedOption = graphicsQuality,
                    optionLabel = { qualityLabel(language, it) },
                    onOptionSelected = onGraphicsQualityChange
                )
            }
        }

        item {
            SettingCard(
                title = t(language, "language"),
                subtitle = "${t(language, "current_language")}: ${languageLabel(language, selectedLanguage)}"
            ) {
                OptionsBar(
                    options = listOf("es", "en", "fr"),
                    selectedOption = selectedLanguage,
                    optionLabel = { languageLabel(language, it) },
                    onOptionSelected = onLanguageChange
                )
            }
        }

        item {
            SettingCard(
                title = t(language, "music"),
                subtitle = "${t(language, "current_volume")}: ${musicVolume.toInt()}%"
            ) {
                VolumeControl(
                    volume = musicVolume,
                    onVolumeChange = onMusicVolumeChange
                )
            }
        }

        item {
            SettingCard(
                title = t(language, "sound_effects"),
                subtitle = "${t(language, "current_volume")}: ${soundVolume.toInt()}%"
            ) {
                VolumeControl(
                    volume = soundVolume,
                    onVolumeChange = onSoundVolumeChange
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C63FF)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D6875)
            )
        }
    }
}

@Composable
fun PackCard(
    language: String,
    pack: GamePack,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val border = if (isSelected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(24.dp),
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF1F0FF) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = t(language, pack.nameKey),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$${pack.priceUsd}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF6C63FF),
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${t(language, "coins")}: ${pack.coins}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${t(language, "bonus")}: ${t(language, pack.bonusKey)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6D6875)
            )

            if (isSelected) {
                Text(
                    text = t(language, "selected"),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF00875A),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PurchaseSummary(
    language: String,
    selectedPack: GamePack?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = t(language, "purchase_summary"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )

            if (selectedPack == null) {
                Text(
                    text = t(language, "no_pack"),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text("${t(language, "product")}: ${t(language, selectedPack.nameKey)}")
                Text("${t(language, "price")}: $${selectedPack.priceUsd}")
                Text(
                    "${t(language, "content")}: ${selectedPack.coins} ${t(language, "coins_lower")} " +
                            "${t(language, "and")} ${t(language, selectedPack.bonusKey)}"
                )
            }
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A0CA3)
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6D6875)
                )
            }

            content()
        }
    }
}

@Composable
fun OptionsBar(
    options: List<String>,
    selectedOption: String,
    optionLabel: (String) -> String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            if (selectedOption == option) {
                Button(
                    onClick = { onOptionSelected(option) },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(optionLabel(option))
                }
            } else {
                OutlinedButton(
                    onClick = { onOptionSelected(option) },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(optionLabel(option))
                }
            }
        }
    }
}

@Composable
fun VolumeControl(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..100f
        )

        Text(
            text = "${volume.toInt()}%",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C63FF)
        )
    }
}

fun normalizeLanguage(value: String?): String {
    return when (value) {
        "es", "Español", "Spanish", "Espagnol" -> "es"
        "en", "Inglés", "English", "Anglais" -> "en"
        "fr", "Francés", "French", "Français" -> "fr"
        else -> "es"
    }
}

fun normalizeQuality(value: String?): String {
    return when (value) {
        "low", "Baja", "Low", "Faible" -> "low"
        "medium", "Media", "Medium", "Moyenne" -> "medium"
        "high", "Alta", "High", "Haute" -> "high"
        "ultra", "Ultra" -> "ultra"
        else -> "high"
    }
}

fun languageLabel(currentLanguage: String, languageCode: String): String {
    return when (currentLanguage) {
        "en" -> when (languageCode) {
            "es" -> "Spanish"
            "en" -> "English"
            "fr" -> "French"
            else -> "Spanish"
        }

        "fr" -> when (languageCode) {
            "es" -> "Espagnol"
            "en" -> "Anglais"
            "fr" -> "Français"
            else -> "Espagnol"
        }

        else -> when (languageCode) {
            "es" -> "Español"
            "en" -> "Inglés"
            "fr" -> "Francés"
            else -> "Español"
        }
    }
}

fun qualityLabel(language: String, qualityCode: String): String {
    return when (language) {
        "en" -> when (qualityCode) {
            "low" -> "Low"
            "medium" -> "Medium"
            "high" -> "High"
            "ultra" -> "Ultra"
            else -> "High"
        }

        "fr" -> when (qualityCode) {
            "low" -> "Faible"
            "medium" -> "Moyenne"
            "high" -> "Haute"
            "ultra" -> "Ultra"
            else -> "Haute"
        }

        else -> when (qualityCode) {
            "low" -> "Baja"
            "medium" -> "Media"
            "high" -> "Alta"
            "ultra" -> "Ultra"
            else -> "Alta"
        }
    }
}

fun t(language: String, key: String): String {
    return when (language) {
        "en" -> when (key) {
            "nav_home" -> "Home"
            "nav_matches" -> "Matches"
            "nav_store" -> "Store"
            "nav_profile" -> "Profile"
            "nav_settings" -> "Settings"

            "welcome" -> "Welcome"
            "level" -> "Level"
            "coins" -> "Coins"
            "coins_lower" -> "coins"
            "won" -> "Won"
            "played" -> "Played"
            "matches" -> "Matches"
            "total" -> "Total"
            "quick_actions" -> "Quick actions"
            "go_store" -> "Go to store"
            "edit_profile" -> "Edit profile"

            "matches_title" -> "Matches"
            "matches_description" -> "Match history and simulation of a new match."
            "victories" -> "Victories"
            "won_sub" -> "Won"
            "played_sub" -> "Played"
            "play_simulated" -> "Play simulated match"
            "match_quick" -> "Quick battle"
            "match_survival" -> "Survival mode"
            "match_competitive" -> "Competitive duel"
            "match_weekly" -> "Weekly challenge"
            "result_win" -> "Victory"
            "result_loss" -> "Defeat"

            "store_title" -> "Game store"
            "store_description" -> "Choose a pack to recharge your account."
            "current_balance" -> "Current balance"
            "available_coins" -> "available coins"
            "pack_initial" -> "Starter Pack"
            "pack_pro" -> "Pro Pack"
            "pack_legendary" -> "Legendary Pack"
            "bonus_initial" -> "+ 50 gems"
            "bonus_pro" -> "+ 150 gems"
            "bonus_legendary" -> "+ 300 gems"
            "bonus" -> "Bonus"
            "selected" -> "Selected"
            "purchase_summary" -> "Purchase summary"
            "no_pack" -> "You have not selected any pack."
            "product" -> "Product"
            "price" -> "Price"
            "content" -> "Content"
            "buy_now" -> "Buy now"
            "and" -> "and"

            "profile_title" -> "Player profile"
            "profile_description" -> "User data and progress inside the game."
            "profile_photo" -> "Profile photo"
            "choose_profile_photo" -> "Choose profile photo"
            "player_without_name" -> "Player without name"
            "email_not_registered" -> "Email not registered"
            "current" -> "Current"
            "balance" -> "Balance"
            "edit_info" -> "Edit information"
            "name" -> "Name"
            "email" -> "Email"
            "bio" -> "Biography"
            "save_profile" -> "Save profile"
            "logout" -> "Log out"
            "player_summary" -> "Player summary"
            "matches_played" -> "Matches played"
            "matches_won" -> "Matches won"
            "performance" -> "Performance"

            "settings_title" -> "Settings"
            "settings_description" -> "Adjust the game experience according to your preferences."
            "graphics" -> "Graphics"
            "current_quality" -> "Current quality"
            "language" -> "Language"
            "current_language" -> "Current language"
            "music" -> "Music"
            "sound_effects" -> "Sound effects"
            "current_volume" -> "Current volume"

            "snackbar_match_won" -> "Simulated match won. +150 coins and +1 level."
            "snackbar_select_pack" -> "You must select a pack first."
            "snackbar_purchase" -> "Simulated purchase"
            "snackbar_added" -> "Added"
            "snackbar_complete_profile" -> "Complete all profile fields."
            "snackbar_profile_saved" -> "Profile saved successfully."
            "snackbar_logout" -> "Session closed successfully."

            else -> key
        }

        "fr" -> when (key) {
            "nav_home" -> "Accueil"
            "nav_matches" -> "Parties"
            "nav_store" -> "Boutique"
            "nav_profile" -> "Profil"
            "nav_settings" -> "Paramètres"

            "welcome" -> "Bienvenue"
            "level" -> "Niveau"
            "coins" -> "Pièces"
            "coins_lower" -> "pièces"
            "won" -> "Gagnées"
            "played" -> "Jouées"
            "matches" -> "Parties"
            "total" -> "Total"
            "quick_actions" -> "Actions rapides"
            "go_store" -> "Aller à la boutique"
            "edit_profile" -> "Modifier le profil"

            "matches_title" -> "Parties"
            "matches_description" -> "Historique des parties et simulation d’une nouvelle partie."
            "victories" -> "Victoires"
            "won_sub" -> "Gagnées"
            "played_sub" -> "Jouées"
            "play_simulated" -> "Jouer une partie simulée"
            "match_quick" -> "Bataille rapide"
            "match_survival" -> "Mode survie"
            "match_competitive" -> "Duel compétitif"
            "match_weekly" -> "Défi hebdomadaire"
            "result_win" -> "Victoire"
            "result_loss" -> "Défaite"

            "store_title" -> "Boutique du jeu"
            "store_description" -> "Sélectionnez un pack pour recharger votre compte."
            "current_balance" -> "Solde actuel"
            "available_coins" -> "pièces disponibles"
            "pack_initial" -> "Pack initial"
            "pack_pro" -> "Pack pro"
            "pack_legendary" -> "Pack légendaire"
            "bonus_initial" -> "+ 50 gemmes"
            "bonus_pro" -> "+ 150 gemmes"
            "bonus_legendary" -> "+ 300 gemmes"
            "bonus" -> "Bonus"
            "selected" -> "Sélectionné"
            "purchase_summary" -> "Résumé de l’achat"
            "no_pack" -> "Vous n’avez sélectionné aucun pack."
            "product" -> "Produit"
            "price" -> "Prix"
            "content" -> "Contenu"
            "buy_now" -> "Acheter maintenant"
            "and" -> "et"

            "profile_title" -> "Profil du joueur"
            "profile_description" -> "Données de l’utilisateur et progression dans le jeu."
            "profile_photo" -> "Photo de profil"
            "choose_profile_photo" -> "Choisir une photo de profil"
            "player_without_name" -> "Joueur sans nom"
            "email_not_registered" -> "Email non enregistré"
            "current" -> "Actuel"
            "balance" -> "Solde"
            "edit_info" -> "Modifier les informations"
            "name" -> "Nom"
            "email" -> "Email"
            "bio" -> "Biographie"
            "save_profile" -> "Enregistrer le profil"
            "logout" -> "Se déconnecter"
            "player_summary" -> "Résumé du joueur"
            "matches_played" -> "Parties jouées"
            "matches_won" -> "Parties gagnées"
            "performance" -> "Performance"

            "settings_title" -> "Paramètres"
            "settings_description" -> "Ajustez l’expérience du jeu selon vos préférences."
            "graphics" -> "Graphismes"
            "current_quality" -> "Qualité actuelle"
            "language" -> "Langue"
            "current_language" -> "Langue actuelle"
            "music" -> "Musique"
            "sound_effects" -> "Effets sonores"
            "current_volume" -> "Volume actuel"

            "snackbar_match_won" -> "Partie simulée gagnée. +150 pièces et +1 niveau."
            "snackbar_select_pack" -> "Vous devez d’abord sélectionner un pack."
            "snackbar_purchase" -> "Achat simulé"
            "snackbar_added" -> "Ajouté"
            "snackbar_complete_profile" -> "Complétez tous les champs du profil."
            "snackbar_profile_saved" -> "Profil enregistré avec succès."
            "snackbar_logout" -> "Session fermée avec succès."

            else -> key
        }

        else -> when (key) {
            "nav_home" -> "Inicio"
            "nav_matches" -> "Partidas"
            "nav_store" -> "Tienda"
            "nav_profile" -> "Perfil"
            "nav_settings" -> "Config."

            "welcome" -> "Bienvenido"
            "level" -> "Nivel"
            "coins" -> "Monedas"
            "coins_lower" -> "monedas"
            "won" -> "Ganadas"
            "played" -> "Jugadas"
            "matches" -> "Partidas"
            "total" -> "Total"
            "quick_actions" -> "Accesos rápidos"
            "go_store" -> "Ir a la tienda"
            "edit_profile" -> "Editar perfil"

            "matches_title" -> "Partidas"
            "matches_description" -> "Historial de partidas y simulación de una nueva partida."
            "victories" -> "Victorias"
            "won_sub" -> "Ganadas"
            "played_sub" -> "Jugadas"
            "play_simulated" -> "Jugar partida simulada"
            "match_quick" -> "Batalla rápida"
            "match_survival" -> "Modo supervivencia"
            "match_competitive" -> "Duelo competitivo"
            "match_weekly" -> "Reto semanal"
            "result_win" -> "Victoria"
            "result_loss" -> "Derrota"

            "store_title" -> "Tienda del juego"
            "store_description" -> "Seleccioná un pack para recargar tu cuenta."
            "current_balance" -> "Saldo actual"
            "available_coins" -> "monedas disponibles"
            "pack_initial" -> "Pack Inicial"
            "pack_pro" -> "Pack Pro"
            "pack_legendary" -> "Pack Legendario"
            "bonus_initial" -> "+ 50 gemas"
            "bonus_pro" -> "+ 150 gemas"
            "bonus_legendary" -> "+ 300 gemas"
            "bonus" -> "Bonus"
            "selected" -> "Seleccionado"
            "purchase_summary" -> "Resumen de compra"
            "no_pack" -> "No has seleccionado ningún pack."
            "product" -> "Producto"
            "price" -> "Precio"
            "content" -> "Contenido"
            "buy_now" -> "Comprar ahora"
            "and" -> "y"

            "profile_title" -> "Perfil de jugador"
            "profile_description" -> "Datos del usuario y progreso dentro del juego."
            "profile_photo" -> "Foto de perfil"
            "choose_profile_photo" -> "Elegir foto de perfil"
            "player_without_name" -> "Jugador sin nombre"
            "email_not_registered" -> "Correo no registrado"
            "current" -> "Actual"
            "balance" -> "Saldo"
            "edit_info" -> "Editar información"
            "name" -> "Nombre"
            "email" -> "Correo"
            "bio" -> "Biografía"
            "save_profile" -> "Guardar perfil"
            "logout" -> "Cerrar sesión"
            "player_summary" -> "Resumen del jugador"
            "matches_played" -> "Partidas jugadas"
            "matches_won" -> "Partidas ganadas"
            "performance" -> "Rendimiento"

            "settings_title" -> "Configuración"
            "settings_description" -> "Ajustá la experiencia del juego según tus preferencias."
            "graphics" -> "Gráficos"
            "current_quality" -> "Calidad actual"
            "language" -> "Idioma"
            "current_language" -> "Idioma actual"
            "music" -> "Música"
            "sound_effects" -> "Efectos de sonido"
            "current_volume" -> "Volumen actual"

            "snackbar_match_won" -> "Partida simulada ganada. +150 monedas y +1 nivel."
            "snackbar_select_pack" -> "Debés seleccionar un pack primero."
            "snackbar_purchase" -> "Compra simulada"
            "snackbar_added" -> "Se agregaron"
            "snackbar_complete_profile" -> "Completá todos los campos del perfil."
            "snackbar_profile_saved" -> "Perfil guardado correctamente."
            "snackbar_logout" -> "Sesión cerrada correctamente."

            else -> key
        }
    }
}

fun obtenerIniciales(nombre: String): String {
    val partes = nombre
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }

    return when {
        partes.isEmpty() -> "J"
        partes.size == 1 -> partes[0].take(2).uppercase()
        else -> "${partes[0].first()}${partes[1].first()}".uppercase()
    }
}

fun calcularRendimiento(ganadas: Int, jugadas: Int): Int {
    if (jugadas == 0) return 0
    return ((ganadas.toDouble() / jugadas.toDouble()) * 100).toInt()
}