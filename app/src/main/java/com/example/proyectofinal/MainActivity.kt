package com.example.proyectofinal

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.proyectofinal.data.resources.MonitorAppLifecycle
import com.example.proyectofinal.ui.navigation.*
import com.example.proyectofinal.ui.theme.BattleIoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Configurar el color de la StatusBar para que coincida con el background
        window.statusBarColor = android.graphics.Color.parseColor("#F7F4FF")
        // Configurar el color del texto/iconos de la StatusBar para que sea oscuro
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        hideSystemBars()

        setContent {
            BattleIoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    MonitorAppLifecycle(lifecycleOwner)
                    AppNavHost()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    private fun hideSystemBars() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }
}
