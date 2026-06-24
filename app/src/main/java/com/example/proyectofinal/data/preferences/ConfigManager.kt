package com.example.proyectofinal.data.preferences

import android.content.Context
import android.content.SharedPreferences

object ConfigManager {
    private const val PREF_NAME = "battle_io_config"
    private const val KEY_BASE_URL = "base_url"
    private const val DEFAULT_BASE_URL = "http://192.168.0.243:8080/api/"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getBaseUrl(): String {
        return prefs.getString(KEY_BASE_URL, DEFAULT_BASE_URL) ?: DEFAULT_BASE_URL
    }

    fun setBaseUrl(url: String) {
        // Asegurar que termina con /
        val normalizedUrl = if (url.endsWith("/")) url else "$url/"
        prefs.edit().putString(KEY_BASE_URL, normalizedUrl).apply()
    }

    fun resetBaseUrl() {
        prefs.edit().remove(KEY_BASE_URL).apply()
    }
}

