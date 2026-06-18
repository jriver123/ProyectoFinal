/**
 * SISTEMA DE MÚSICA DE FONDO POR PANTALLA
 * ===========================================
 * 
 * Este sistema gestiona la reproducción de música en loop dependiendo de la pantalla activa.
 * La música se pausa cuando la app va al background y se reanuda cuando vuelve al foreground.
 * 
 * ARCHIVOS PRINCIPALES:
 * ---------------------
 * 
 * 1. BackgroundMusicPlayer.kt
 *    - Singleton que gestiona la reproducción de música
 *    - Contiene el mapeo de pantallas a archivos de música
 *    - Proporciona métodos: playScreenMusic(), stopMusic(), pauseMusic(), resumeMusic(), setVolume()
 * 
 * 2. LifecycleExtensions.kt
 *    - Monitorea el ciclo de vida de la app
 *    - Pausa música en ON_PAUSE, reanuda en ON_RESUME
 *    - Se integra automáticamente en MainActivity
 * 
 * 3. AppNavHost.kt
 *    - Reproduce música cuando cambia de ruta (pantalla)
 *    - Aplica volumen de música desde settings en tiempo real
 * 
 * CÓMO AGREGAR MÚSICA A UNA PANTALLA:
 * ===================================
 * 
 * 1. Añade el archivo de audio a res/raw/ (ej: home_bg.mp3, store_bg.mp3)
 * 
 * 2. En BackgroundMusicPlayer.kt, agrega la referencia en screenMusicMap:
 *    
 *    private val screenMusicMap: Map<String, Int> = mapOf(
 *        ScreenBgMusic.Battle to R.raw.battle_screen_bg,  // Existente
 *        ScreenBgMusic.Home to R.raw.home_bg,              // Nuevo
 *        ScreenBgMusic.Store to R.raw.store_bg             // Nuevo
 *    )
 * 
 * 3. En AppNavHost.kt, agrega el caso en el LaunchedEffect(currentRoute):
 *    
 *    AppRoutes.Home -> {
 *        BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Home)
 *    }
 * 
 * EJEMPLOS DE USO:
 * ===============
 * 
 * // Reproducir música de una pantalla
 * BackgroundMusicPlayer.playScreenMusic(context, ScreenBgMusic.Battle)
 * 
 * // Pausar la música actual
 * BackgroundMusicPlayer.pauseMusic()
 * 
 * // Reanudar la música
 * BackgroundMusicPlayer.resumeMusic()
 * 
 * // Detener la música
 * BackgroundMusicPlayer.stopMusic()
 * 
 * // Cambiar el volumen (0.0 a 1.0)
 * BackgroundMusicPlayer.setVolume(0.5f)
 * 
 * CARACTERÍSTICAS:
 * ===============
 * 
 * ✓ Solo una canción activa a la vez
 * ✓ Reproducción automática en loop
 * ✓ Control de volumen desde Settings
 * ✓ Pausa automática cuando app va al background
 * ✓ Reanuda automáticamente cuando vuelve al foreground
 * ✓ Manejo automático de errores
 * ✓ Logs para debugging
 * 
 * NOTAS:
 * =====
 * 
 * - La música se sincroniza con la pantalla actual automáticamente
 * - El volumen se puede controlar desde Settings y se aplica en tiempo real
 * - Los cambios en la orientación no interrumpen la música
 * - Solo pausará si la app realmente se minimiza
 */

