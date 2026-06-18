package com.example.proyectofinal.data.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

// ============================================================================
// MONITOREO DEL CICLO DE VIDA DE LA APP
// ============================================================================

/**
 * Efecto Composable que monitorea el ciclo de vida de la app.
 * Pausa la música cuando la app va al background y la reanuda cuando
 * vuelve al foreground.
 */
@Composable
fun MonitorAppLifecycle(lifecycleOwner: LifecycleOwner) {
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    BackgroundMusicPlayer.pauseMusic()
                }
                Lifecycle.Event.ON_RESUME -> {
                    BackgroundMusicPlayer.resumeMusic()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    BackgroundMusicPlayer.stopMusic()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}

