package com.example.proyectofinal

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.remote.RetrofitClient
import com.example.proyectofinal.data.resources.*
import com.example.proyectofinal.ui.screen.*
import com.example.proyectofinal.ui.theme.BattleIoTheme
import com.example.proyectofinal.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import kotlin.jvm.java
import com.example.proyectofinal.data.repository.*
import com.example.proyectofinal.viewmodel.UsuarioViewModelFactory

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
fun BattleIoApp() {
    val context = LocalContext.current
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()
    val repository = UsuarioRepository(RetrofitClient.usuarioApi, db.usuarioLoginDao())
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(repository)
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val usuarioUIState by usuarioViewModel.uiState.collectAsState()
    var startDestination by remember { mutableStateOf("login") }
    val prefs = remember { context.getSharedPreferences("battle_io_data", Context.MODE_PRIVATE) }
    // Estados locales (solo settings)
    var graphicsQuality by rememberSaveable {
        mutableStateOf(prefs.getString("graphicsQuality", "high") ?: "high")
    }
    var selectedLanguage by rememberSaveable {
        mutableStateOf(prefs.getString("selectedLanguage", "es") ?: "es")
    }
    var musicVolume by rememberSaveable {
        mutableFloatStateOf(prefs.getFloat("musicVolume", 75f))
    }
    var soundVolume by rememberSaveable {
        mutableFloatStateOf(prefs.getFloat("soundVolume", 75f))
    }

    // Estados que vienen del backend (UsuarioDTO)
    val usuarioActivo = usuarioUIState.usuarioActivo

    LaunchedEffect(Unit) {
        usuarioViewModel.verificarLoginAutomatico { usuarioGuardado ->
            startDestination = if (usuarioGuardado != null) "home" else "login"
        }
    }
    LaunchedEffect(usuarioActivo) {
        usuarioActivo?.let { usuario ->
            // Aquí sincronizas datos del backend con la UI
            println("Usuario activo: ${usuario.username}, nivel: ${usuario.nivel}")
        }
    }

    // Guardar cambios en prefs cuando settings cambien
    LaunchedEffect(graphicsQuality, selectedLanguage, musicVolume, soundVolume) {
        prefs.edit {
            putString("graphicsQuality", graphicsQuality)
            putString("selectedLanguage", selectedLanguage)
            putFloat("musicVolume", musicVolume)
            putFloat("soundVolume", soundVolume)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(SnackbarHostState()) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    icon = { Text("🏠") },
                    label = { Text(t("es", "nav_home")) }
                )
                NavigationBarItem(
                    selected = currentRoute == "matches" || currentRoute == "story" || currentRoute == "combat",
                    onClick = { navController.navigate("matches") },
                    icon = { Text("⚔️") },
                    label = { Text(t("es", "nav_matches")) }
                )
                NavigationBarItem(
                    selected = currentRoute == "store",
                    onClick = { navController.navigate("store") },
                    icon = { Text("🛒") },
                    label = { Text(t("es", "nav_store")) }
                )
                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = { navController.navigate("profile") },
                    icon = { Text("👤") },
                    label = { Text(t("es", "nav_profile")) }
                )
                NavigationBarItem(
                    selected = currentRoute == "settings",
                    onClick = { navController.navigate("settings") },
                    icon = { Text("⚙️") },
                    label = { Text(t("es", "nav_settings")) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable("login") {
                    LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginSuccess = { response ->
                            usuarioViewModel.cargarUsuarioDetalles(response.id)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                composable("home") {
                    val usuarioUIState by usuarioViewModel.uiState.collectAsState()
                    usuarioUIState.usuarioActivo?.let { usuario ->
                        HomeScreen(
                            usuario = usuario,
                            language = "es",
                            profileImageUri = null,
                            onGoToStore = { navController.navigate("store") },
                            onGoToProfile = { navController.navigate("profile") },
                            onGoToStory = { navController.navigate("story") }
                        )
                    }
                }
                composable("matches") {
                    val usuarioActivo = usuarioUIState.usuarioActivo
                    if (usuarioActivo != null) {
                        MatchesScreen(
                            usuario = usuarioActivo,
                            language = selectedLanguage,
                            matches = listOf(),
                            navController = navController,
                            onPlayMatch = {
                                // lógica para simular partida
                                println("Simulación de partida iniciada")
                            },
                            onGoToStory = {
                                navController.navigate("story")
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable("store") {
                    val uiState by usuarioViewModel.uiState.collectAsState()
                    uiState.usuarioActivo?.let { usuario ->
                        StoreScreen(
                            usuario = usuario,
                            language = "es",
                            packs = listOf(/* tus GamePack */),
                            selectedPackId = null,
                            navController = navController,
                            onSelectPack = { packId -> /* lógica de selección */ },
                            onPurchase = { /* lógica de compra, actualizar monedas */ }
                        )
                    }
                }
                composable("settings") {
                    val uiState by usuarioViewModel.uiState.collectAsState()
                    uiState.usuarioActivo?.let { usuario ->
                        SettingsScreen(
                            usuario = usuario,
                            language = "es",
                            graphicsQuality = "high", // o lo que tengas en prefs
                            selectedLanguage = "es",
                            musicVolume = 75f,
                            soundVolume = 75f,
                            navController = navController,
                            onGraphicsQualityChange = { /* guardar en prefs */ },
                            onLanguageChange = { /* guardar en prefs */ },
                            onMusicVolumeChange = { /* guardar en prefs */ },
                            onSoundVolumeChange = { /* guardar en prefs */ }
                        )
                    }
                }
                composable("profile") {
                    val usuarioActivo = usuarioUIState.usuarioActivo
                    if (usuarioActivo != null) {
                        ProfileScreen(
                            usuario = usuarioActivo,
                            language = selectedLanguage,
                            navController = navController,
                            isLoadingUsuario = usuarioUIState.isLoading,
                            apiMessage = usuarioUIState.message,
                            apiError = usuarioUIState.errorMessage,
                            onNombreChange = { nuevoNombre -> /* actualizar en ViewModel */ },
                            onCorreoChange = { nuevoCorreo -> /* actualizar en ViewModel */ },
                            onBioChange = { nuevaBio -> /* actualizar en ViewModel */ },
                            onPasswordChange = { nuevaPass -> /* actualizar en ViewModel */ },
                            onProfileImageChange = { nuevaImagen -> /* actualizar en ViewModel */ },
                            onRefreshUsuario = {
                                usuarioViewModel.cargarUsuarioDetalles(
                                    usuarioActivo.id
                                )
                            },
                            onSave = {
                                usuarioViewModel.actualizarUsuario(
                                    usuarioActivo.id,
                                    usuarioActivo
                                )
                            },
                            onDeleteUsuario = { usuarioViewModel.eliminarUsuario(usuarioActivo.id) },
                            onLogout = {
                                usuarioViewModel.logout {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable("story") {
                    val uiState by usuarioViewModel.uiState.collectAsState()
                    val usuarioActivo = uiState.usuarioActivo

                    if (usuarioActivo != null) {
                        StoryScreen(
                            storyProgress = usuarioActivo.storyProgress,
                            onStart = {
                                // Aquí navegas a la pantalla de selección de personaje
                                navController.navigate("character_select")
                            },
                            onBack = {
                                // Vuelve a la pantalla de partidas
                                navController.navigate("matches") {
                                    popUpTo("home") { inclusive = false }
                                }
                            },
                            onResetStory = {
                                usuarioViewModel.resetStory(usuarioActivo.id)
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable("character_select") {
                    // Llamamos directamente a la función
                    val characters = getPlayableCharacters()

                    var selectedCharacterId by rememberSaveable { mutableStateOf(-1) }

                    CharacterSelectionScreen(
                        characters = characters,
                        selectedCharacterId = selectedCharacterId,
                        onSelectCharacter = { id -> selectedCharacterId = id },
                        onStartBattle = {
                            if (selectedCharacterId != -1) {
                                navController.navigate("battle/$selectedCharacterId")
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("battle/{characterId}") { backStackEntry ->
                    val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                    val characters = getPlayableCharacters()
                    val player = characters.firstOrNull { it.id == characterId } ?: characters.first()
                    val chapter = getStoryChapters().first() // puedes usar el progreso real del usuario
                    val enemy = chapter.enemy

                    var playerHp by rememberSaveable { mutableStateOf(player.maxHp) }
                    var enemyHp by rememberSaveable { mutableStateOf(enemy.maxHp) }
                    var battleMessage by rememberSaveable { mutableStateOf("¡El combate comienza!") }
                    var battleFinished by rememberSaveable { mutableStateOf(false) }

                    BattleScreen(
                        player = player,
                        enemy = enemy,
                        chapter = chapter,
                        playerHp = playerHp,
                        enemyHp = enemyHp,
                        battleMessage = battleMessage,
                        battleFinished = battleFinished,
                        onAttack = { attack ->
                            // Lógica simple de combate
                            enemyHp -= attack.damage + player.attackBonus
                            battleMessage = "Usaste ${attack.name} y causaste ${attack.damage} de daño."
                            if (enemyHp <= 0) {
                                battleMessage = "¡Has derrotado al enemigo!"
                                battleFinished = true
                            }
                        },
                        onExit = { navController.navigate("matches") },
                        onRetry = {
                            playerHp = player.maxHp
                            enemyHp = enemy.maxHp
                            battleMessage = "¡El combate comienza de nuevo!"
                            battleFinished = false
                        }
                    )
                }
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF3A0CA3))
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