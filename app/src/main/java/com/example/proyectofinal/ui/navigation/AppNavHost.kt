package com.example.proyectofinal.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.proyectofinal.data.local.AppDatabase
import com.example.proyectofinal.data.remote.RetrofitClient
import com.example.proyectofinal.data.repository.SettingsPreferences
import com.example.proyectofinal.data.repository.SettingsRepository
import com.example.proyectofinal.data.repository.UsuarioRepository
import com.example.proyectofinal.data.resources.AppDefaults
import com.example.proyectofinal.data.resources.BackgroundMusicPlayer
import com.example.proyectofinal.data.resources.GetTranssStorySec
import com.example.proyectofinal.data.resources.ScreenBgMusic
import com.example.proyectofinal.data.resources.getPlayableCharacters
import com.example.proyectofinal.data.resources.getStoryChapters
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.screen.BattleScreen
import com.example.proyectofinal.ui.screen.CharacterSelectionScreen
import com.example.proyectofinal.ui.screen.HomeScreen
import com.example.proyectofinal.ui.screen.LoginScreen
import com.example.proyectofinal.ui.screen.MatchesScreen
import com.example.proyectofinal.ui.screen.ProfileScreen
import com.example.proyectofinal.ui.screen.RegisterScreen
import com.example.proyectofinal.ui.screen.SettingsScreen
import com.example.proyectofinal.ui.screen.StoryScreen
import com.example.proyectofinal.ui.screen.StoreScreen
import com.example.proyectofinal.ui.screen.TransssStoryScreen
import com.example.proyectofinal.viewmodel.BattleViewModel
import com.example.proyectofinal.viewmodel.UsuarioViewModel
import com.example.proyectofinal.viewmodel.UsuarioViewModelFactory
import kotlinx.coroutines.launch


@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()
    val settingsRepository = remember { SettingsRepository(context.applicationContext) }
    val repository = UsuarioRepository(
        RetrofitClient.usuarioApi,
        db.usuarioLoginDao(),
        db.heroProgressDao()
    )
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(repository)
    )
    val settings by settingsRepository.settingsFlow.collectAsState(initial = SettingsPreferences())
    val heroRoster = remember {
        getPlayableCharacters().associateBy { it.id }.toMutableMap()
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val usuarioUIState by usuarioViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val topLevelRoutes = remember {
        setOf(
            AppRoutes.Home,
            AppRoutes.Matches,
            AppRoutes.Store,
            AppRoutes.Profile,
            AppRoutes.Settings
        )
    }
    val showBottomBar = currentRoute in topLevelRoutes
    val graphicsQuality = settings.graphicsQuality
    val selectedLanguage = settings.selectedLanguage
    val musicVolume = settings.musicVolume
    val soundVolume = settings.soundVolume
    val selectedPackId = settings.selectedPackId

    val usuarioActivo = usuarioUIState.usuarioActivo

    LaunchedEffect(musicVolume) {
        BackgroundMusicPlayer.setVolume(musicVolume / 100f)
    }

    LaunchedEffect(Unit) {
        usuarioViewModel.verificarLoginAutomatico { usuarioGuardado ->
            if (usuarioGuardado != null) {
                usuarioViewModel.cargarUsuarioDetalles(usuarioGuardado.id)
                navController.navigate(AppRoutes.Home) {
                    popUpTo(AppRoutes.Login) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    LaunchedEffect(usuarioActivo) {
        usuarioActivo?.let { usuario ->
            println("Usuario activo: ${usuario.username}, nivel: ${usuario.nivel}")
        }
    }

    LaunchedEffect(usuarioUIState.message, usuarioUIState.errorMessage) {
        val feedback = usuarioUIState.errorMessage ?: usuarioUIState.message
        if (!feedback.isNullOrBlank()) {
            snackbarHostState.showSnackbar(feedback)
            usuarioViewModel.clearFeedback()
        }
    }

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            AppRoutes.BattleWithArg -> {
                // La música de batalla/derrota se controla dentro del composable de batalla.
            }
            AppRoutes.Home -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Home)
                // Volumen normal
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Matches -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Home)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Store -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Store)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Profile -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Profile)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Settings -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Settings)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Story -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Story)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            AppRoutes.Login -> {
                BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Login)
                BackgroundMusicPlayer.setVolume(musicVolume / 100f)
            }
            else -> {
                // Detener música en rutas desconocidas o transiciones
            }
        }
    }

    LaunchedEffect(usuarioActivo?.id) {
        val userId = usuarioActivo?.id ?: return@LaunchedEffect
        val mergedHeroes = repository.loadHeroProgress(userId, heroRoster.values)
        mergedHeroes.forEach { loadedHero ->
            heroRoster[loadedHero.id] = loadedHero
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == AppRoutes.Home,
                    onClick = {
                        navController.navigate(AppRoutes.Home) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Text("🏠") },
                    label = { Text(t(selectedLanguage, "nav_home")) }
                )
                NavigationBarItem(
                    selected = currentRoute == AppRoutes.Matches || currentRoute == AppRoutes.Story || currentRoute?.startsWith(
                        AppRoutes.BattleBase
                    ) == true,
                    onClick = {
                        navController.navigate(AppRoutes.Matches) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Text("⚔️") },
                    label = { Text(t(selectedLanguage, "nav_matches")) }
                )
                NavigationBarItem(
                    selected = currentRoute == AppRoutes.Store,
                    onClick = {
                        navController.navigate(AppRoutes.Store) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Text("🛒") },
                    label = { Text(t(selectedLanguage, "nav_store")) }
                )
                NavigationBarItem(
                    selected = currentRoute == AppRoutes.Profile,
                    onClick = {
                        navController.navigate(AppRoutes.Profile) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Text("👤") },
                    label = { Text(t(selectedLanguage, "nav_profile")) }
                )
                NavigationBarItem(
                    selected = currentRoute == AppRoutes.Settings,
                    onClick = {
                        navController.navigate(AppRoutes.Settings) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Text("⚙️") },
                    label = { Text(t(selectedLanguage, "nav_settings")) }
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
                startDestination = AppRoutes.Login
            ) {
                composable(AppRoutes.Login) {
                    LoginScreen(
                        viewModel = usuarioViewModel,
                        onLoginSuccess = { response ->
                            usuarioViewModel.cargarUsuarioDetalles(response.id)
                            navController.navigate(AppRoutes.Home) {
                                popUpTo(AppRoutes.Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onRegisterClick = {
                            navController.navigate(AppRoutes.Register)
                        }
                    )
                }
                composable(AppRoutes.Register) {
                    RegisterScreen(
                        viewModel = usuarioViewModel,
                        language = selectedLanguage,
                        onRegisterSuccess = { response ->
                            usuarioViewModel.cargarUsuarioDetalles(response.id)
                            navController.navigate(AppRoutes.Home) {
                                popUpTo(AppRoutes.Login) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onBackToLogin = { navController.popBackStack() }
                    )
                }
                composable(AppRoutes.Home) {
                    val homeUiState by usuarioViewModel.uiState.collectAsState()
                    homeUiState.usuarioActivo?.let { usuario ->
                        HomeScreen(
                            usuario = usuario,
                            language = selectedLanguage,
                            profileImageUri = null,
                            onGoToStore = { navController.navigate(AppRoutes.Store) },
                            onGoToProfile = { navController.navigate(AppRoutes.Profile) },
                            onGoToStory = { navController.navigate(AppRoutes.Story) }
                        )
                    }
                }
                composable(AppRoutes.Matches) {
                    val matchesUsuario = usuarioUIState.usuarioActivo
                    if (matchesUsuario != null) {
                        MatchesScreen(
                            usuario = matchesUsuario,
                            language = selectedLanguage,
                            matches = AppDefaults.DemoMatches,
                            navController = navController,
                            onPlayMatch = {
                                println("Simulacion de partida iniciada")
                            },
                            onGoToStory = {
                                navController.navigate(AppRoutes.Story)
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable(AppRoutes.Store) {
                    val storeUiState by usuarioViewModel.uiState.collectAsState()
                    storeUiState.usuarioActivo?.let { usuario ->
                        StoreScreen(
                            usuario = usuario,
                            language = selectedLanguage,
                            packs = AppDefaults.DemoPacks,
                            selectedPackId = selectedPackId,
                            navController = navController,
                            onSelectPack = { packId ->
                                scope.launch { settingsRepository.setSelectedPackId(packId) }
                            },
                            onPurchase = { }
                        )
                    }
                }
                composable(AppRoutes.Settings) {
                    val settingsUiState by usuarioViewModel.uiState.collectAsState()
                    settingsUiState.usuarioActivo?.let { usuario ->
                        SettingsScreen(
                            usuario = usuario,
                            language = selectedLanguage,
                            graphicsQuality = graphicsQuality,
                            selectedLanguage = selectedLanguage,
                            musicVolume = musicVolume,
                            soundVolume = soundVolume,
                            navController = navController,
                            onGraphicsQualityChange = { value ->
                                scope.launch { settingsRepository.setGraphicsQuality(value) }
                            },
                            onLanguageChange = { value ->
                                scope.launch { settingsRepository.setLanguage(value) }
                            },
                            onMusicVolumeChange = { value ->
                                scope.launch { settingsRepository.setMusicVolume(value) }
                            },
                            onSoundVolumeChange = { value ->
                                scope.launch { settingsRepository.setSoundVolume(value) }
                            }
                        )
                    }
                }
                composable(AppRoutes.Profile) {
                    val profileUsuario = usuarioUIState.usuarioActivo
                    if (profileUsuario != null) {
                        var profileNombre by rememberSaveable(profileUsuario.id) {
                            mutableStateOf(profileUsuario.username)
                        }
                        var profileCorreo by rememberSaveable(profileUsuario.id) {
                            mutableStateOf(profileUsuario.email)
                        }
                        var profileBio by rememberSaveable(profileUsuario.id) {
                            mutableStateOf(profileUsuario.description.orEmpty())
                        }
                        var profilePassword by rememberSaveable(profileUsuario.id) {
                            mutableStateOf(profileUsuario.password)
                        }

                        LaunchedEffect(profileUsuario) {
                            profileNombre = profileUsuario.username
                            profileCorreo = profileUsuario.email
                            profileBio = profileUsuario.description.orEmpty()
                            profilePassword = profileUsuario.password
                        }

                        val profileDraft = profileUsuario.copy(
                            username = profileNombre,
                            email = profileCorreo,
                            description = profileBio,
                            password = profilePassword
                        )

                        ProfileScreen(
                            usuario = profileDraft,
                            language = selectedLanguage,
                            navController = navController,
                            isLoadingUsuario = usuarioUIState.isLoading,
                            onNombreChange = { profileNombre = it },
                            onCorreoChange = { profileCorreo = it },
                            onBioChange = { profileBio = it },
                            onPasswordChange = { profilePassword = it },
                            onProfileImageChange = { _ -> },
                            onRefreshUsuario = {
                                usuarioViewModel.cargarUsuarioDetalles(profileUsuario.id)
                            },
                            onSave = {
                                usuarioViewModel.actualizarUsuario(profileUsuario.id, profileDraft)
                            },
                            onDeleteUsuario = {
                                usuarioViewModel.eliminarUsuario(profileUsuario.id)
                            },
                            onLogout = {
                                usuarioViewModel.logout {
                                    navController.navigate(AppRoutes.Login) {
                                        popUpTo(AppRoutes.Home) { inclusive = true }
                                    }
                                }
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable(AppRoutes.Story) {
                    val storyUiState by usuarioViewModel.uiState.collectAsState()
                    val storyUsuario = storyUiState.usuarioActivo

                    if (storyUsuario != null) {
                        StoryScreen(
                            storyProgress = storyUsuario.storyProgress,
                            onStart = {
                                navController.navigate(AppRoutes.TranssStory)
                            },
                            onBack = {
                                navController.navigate(AppRoutes.Matches) {
                                    popUpTo(AppRoutes.Home) { inclusive = false }
                                }
                            },
                            onResetStory = {
                                usuarioViewModel.resetStory(storyUsuario.id)
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable(AppRoutes.TranssStory) {
                    val storyUiState by usuarioViewModel.uiState.collectAsState()
                    val storyUsuario = storyUiState.usuarioActivo

                    if (storyUsuario != null) {
                        val chapters = getStoryChapters()
                        val chapterId = chapters
                            .firstOrNull { it.id == storyUsuario.storyProgress }
                            ?.id ?: chapters.first().id

                        val section = GetTranssStorySec(chapterId)
                        val player = heroRoster.values.firstOrNull()
                            ?: getPlayableCharacters().first()

                        TransssStoryScreen(
                            player = player,
                            section = section,
                            onOptionSelected = { _, _ ->
                                navController.navigate(AppRoutes.CharacterSelect)
                            },
                            onContinue = {
                                navController.navigate(AppRoutes.CharacterSelect)
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    } else {
                        Text("No hay usuario activo")
                    }
                }
                composable(AppRoutes.CharacterSelect) {
                    val characters = heroRoster.values.toList()

                    var selectedCharacterId by rememberSaveable { mutableStateOf(-1) }

                    CharacterSelectionScreen(
                        characters = characters,
                        selectedCharacterId = selectedCharacterId,
                        onSelectCharacter = { id -> selectedCharacterId = id },
                        onStartBattle = {
                            if (selectedCharacterId != -1) {
                                navController.navigate(AppRoutes.battle(selectedCharacterId))
                            }
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(AppRoutes.BattleWithArg) { backStackEntry ->
                    val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                    val characters = heroRoster.values.toList()
                    val player = characters.firstOrNull { it.id == characterId } ?: characters.first()
                    val battleStoryProgress = usuarioUIState.usuarioActivo?.storyProgress ?: 1
                    val allChapters = getStoryChapters()
                    val chapter = allChapters.firstOrNull { it.id == battleStoryProgress }
                        ?: allChapters.first()
                    val enemies = chapter.enemies

                    val battleViewModel: BattleViewModel = viewModel()

                    LaunchedEffect(Unit) {
                        battleViewModel.iniciarCombate(player, enemies)
                    }

                    LaunchedEffect(
                        battleViewModel.battleFinished,
                        battleViewModel.battleResultResolved,
                        battleViewModel.userRewardRegistered
                    ) {
                        if (
                            battleViewModel.battleFinished &&
                            battleViewModel.battleResultResolved &&
                            !battleViewModel.userRewardRegistered
                        ) {
                            val userCoins = if (battleViewModel.playerWon) {
                                (chapter.rewardCoins / 4).coerceAtLeast(15)
                            } else {
                                (chapter.rewardCoins / 10).coerceAtLeast(5)
                            }
                            val userXp = if (battleViewModel.playerWon) {
                                (chapter.rewardXp / 4).coerceAtLeast(10)
                            } else {
                                (chapter.rewardXp / 10).coerceAtLeast(4)
                            }

                            usuarioViewModel.registrarResultadoPartida(
                                victoria = battleViewModel.playerWon,
                                monedasGanadas = userCoins,
                                xpGanada = userXp,
                                onResult = { registrado ->
                                    usuarioActivo?.id?.let { userId ->
                                        scope.launch {
                                            repository.saveHeroProgress(userId, player)
                                        }
                                    }
                                    if (registrado) {
                                        battleViewModel.registrarRecompensaUsuario()
                                    }
                                }
                            )
                        }
                    }

                    val battleMusicKey = when {
                        battleViewModel.battleFinished && battleViewModel.playerWon -> ScreenBgMusic.Victory
                        battleViewModel.battleFinished && !battleViewModel.playerWon -> ScreenBgMusic.Defeat
                        else -> ScreenBgMusic.Battle
                    }

                    LaunchedEffect(battleMusicKey, musicVolume) {
                        BackgroundMusicPlayer.playScreenMusic(context, battleMusicKey)
                        BackgroundMusicPlayer.setVolume((musicVolume / 100f) * 0.7f)
                    }

                    BattleScreen(
                        player = player,
                        enemigos = enemies,
                        chapter = chapter,
                        isPlayerTurn = battleViewModel.isPlayerTurn,
                        isResolvingTurn = battleViewModel.isResolvingTurn,
                        playerHp = battleViewModel.playerHp,
                        playerXP = battleViewModel.playerXP,
                        playerNextLevelXP = battleViewModel.playerNextLevelXP,
                        playerLevel = battleViewModel.playerLevel,
                        playerAttackIds = battleViewModel.playerAttacks,
                        enemyHpMap = battleViewModel.enemyHpMap,
                        battleMessage = battleViewModel.battleMessage,
                        battleFinished = battleViewModel.battleFinished,
                        requiresSkillSelection = battleViewModel.requiresSkillSelection,
                        pendingSkillChoices = battleViewModel.pendingSkillChoices,
                        soundCue = battleViewModel.soundCue,
                        onSoundConsumed = { battleViewModel.consumeSoundCue() },
                        onAttack = { attack, targetId ->
                            battleViewModel.atacar(player, enemies, targetId, ataque = attack)
                        },
                        onSelectSkill = { attackId ->
                            battleViewModel.seleccionarNuevaHabilidad(player, attackId)
                        },
                        onExit = { navController.navigate(AppRoutes.Matches) },
                        onRetry = { battleViewModel.reiniciar(player, enemies) }
                    )
                }

            }
        }
    }
}
