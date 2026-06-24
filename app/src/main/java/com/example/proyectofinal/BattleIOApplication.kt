package com.example.proyectofinal

import android.app.Application
import com.example.proyectofinal.data.preferences.ConfigManager

class BattleIOApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ConfigManager.init(this)
    }
}

