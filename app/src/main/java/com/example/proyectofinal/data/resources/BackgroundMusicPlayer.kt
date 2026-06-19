package com.example.proyectofinal.data.resources

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.proyectofinal.R

// ============================================================================
// MAPEO CENTRALIZADO DE PANTALLAS A MÚSICA DE FONDO
// ============================================================================

object ScreenBgMusic {
    const val Battle = "battle"
    const val Defeat = "defeat"
    const val Victory = "victory"
    const val Home = "home"
    const val Store = "store"
    const val Profile = "profile"
    const val Settings = "settings"
    const val Story = "story"
    const val Login = "login"
}

private val screenMusicMap: Map<String, Int> = mapOf(
    ScreenBgMusic.Battle to R.raw.battle_bg,
    ScreenBgMusic.Defeat to R.raw.defeat_sound,
    ScreenBgMusic.Victory to R.raw.victory_sound,
    ScreenBgMusic.Login to R.raw.login_bg,
    ScreenBgMusic.Home to R.raw.main_bg,
    ScreenBgMusic.Store to R.raw.main_bg,
    ScreenBgMusic.Profile to R.raw.main_bg,
    ScreenBgMusic.Settings to R.raw.main_bg,
    // Agregar más pantallas aquí: ScreenBgMusic.Home to R.raw.home_bg, etc.
)

// ============================================================================
// SINGLETON PARA GESTIONAR MÚSICA EN LOOP
// ============================================================================

object BackgroundMusicPlayer {
    private var currentMediaPlayer: MediaPlayer? = null
    private var currentScreen: String? = null
    private var currentMusicRes: Int? = null

    fun playScreenMusic(context: Context, screenKey: String) {
        val soundRes = screenMusicMap[screenKey]

        // Si ya es la misma pista de audio, no reiniciar para evitar cortes de audio.
        if (currentMusicRes == soundRes && currentMediaPlayer != null) {
            if (currentMediaPlayer?.isPlaying == false) {
                currentMediaPlayer?.start()
            }
            currentScreen = screenKey
            Log.d("BgMusicPlayer", "Continuando con la misma música para pantalla: $screenKey")
            return
        }

        // Detener música anterior si estamos cambiando de pista
        if (currentMusicRes != soundRes) {
            stopMusic()
        }

        // Si no hay música para esta pantalla, solo se detiene la anterior y listo
        if (soundRes == null) {
            Log.d("BgMusicPlayer", "No hay música configurada para la pantalla: $screenKey")
            return
        }

        currentMediaPlayer = MediaPlayer.create(context, soundRes)
        currentMediaPlayer?.let {
            it.isLooping = true
            it.setOnErrorListener { mp, _, _ ->
                mp.release()
                currentMediaPlayer = null
                true
            }
            it.start()
            currentScreen = screenKey
            currentMusicRes = soundRes
            Log.d("BgMusicPlayer", "Reproduciendo música para pantalla: $screenKey")
        } ?: run {
            Log.e("BgMusicPlayer", "No se pudo crear MediaPlayer para $screenKey")
        }
    }

    fun stopMusic() {
        currentMediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            } catch (e: Exception) {
                Log.e("BgMusicPlayer", "Error al detener música: ${e.message}")
            }
        }
        currentMediaPlayer = null
        currentScreen = null
        currentMusicRes = null
        Log.d("BgMusicPlayer", "Música detenida")
    }

    fun pauseMusic() {
        currentMediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.pause()
                }
            } catch (e: Exception) {
                Log.e("BgMusicPlayer", "Error al pausar música: ${e.message}")
            }
        }
    }

    fun resumeMusic() {
        currentMediaPlayer?.let {
            try {
                if (!it.isPlaying) {
                    it.start()
                }
            } catch (e: Exception) {
                Log.e("BgMusicPlayer", "Error al reanudar música: ${e.message}")
            }
        }
    }

    fun setVolume(volume: Float) {
        currentMediaPlayer?.let {
            try {
                // Volume debe estar entre 0.0 y 1.0
                val normalizedVolume = volume.coerceIn(0f, 1f)
                it.setVolume(normalizedVolume, normalizedVolume)
            } catch (e: Exception) {
                Log.e("BgMusicPlayer", "Error al ajustar volumen: ${e.message}")
            }
        }
    }
}

