package com.example.proyectofinal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyectofinal.data.resources.AppDefaults
import com.example.proyectofinal.data.resources.normalizeLanguage
import com.example.proyectofinal.data.resources.normalizeQuality
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
    name = AppDefaults.PrefsFile
)

data class SettingsPreferences(
    val graphicsQuality: String = AppDefaults.DefaultGraphicsQuality,
    val selectedLanguage: String = AppDefaults.DefaultSelectedLanguage,
    val musicVolume: Float = AppDefaults.DefaultMusicVolume,
    val soundVolume: Float = AppDefaults.DefaultSoundVolume,
    val selectedPackId: Int? = null,
    val unlockedHeroIds: Set<Int> = AppDefaults.DefaultUnlockedHeroIds
)

class SettingsRepository(private val context: Context) {

    private object Keys {
        val graphicsQuality = stringPreferencesKey(AppDefaults.KeyGraphicsQuality)
        val selectedLanguage = stringPreferencesKey(AppDefaults.KeySelectedLanguage)
        val musicVolume = floatPreferencesKey(AppDefaults.KeyMusicVolume)
        val soundVolume = floatPreferencesKey(AppDefaults.KeySoundVolume)
        val selectedPackId = intPreferencesKey(AppDefaults.KeySelectedPackId)
        val unlockedHeroIds = stringSetPreferencesKey(AppDefaults.KeyUnlockedHeroIds)
    }

    val settingsFlow: Flow<SettingsPreferences> = context.appDataStore.data.map { prefs ->
        val storedPackId = prefs[Keys.selectedPackId]
        val unlockedHeroes = prefs[Keys.unlockedHeroIds]
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            .orEmpty() + AppDefaults.DefaultUnlockedHeroIds
        SettingsPreferences(
            graphicsQuality = normalizeQuality(prefs[Keys.graphicsQuality]),
            selectedLanguage = normalizeLanguage(prefs[Keys.selectedLanguage]),
            musicVolume = prefs[Keys.musicVolume] ?: AppDefaults.DefaultMusicVolume,
            soundVolume = prefs[Keys.soundVolume] ?: AppDefaults.DefaultSoundVolume,
            selectedPackId = if (
                storedPackId == null || storedPackId == AppDefaults.DefaultSelectedPackId
            ) {
                null
            } else {
                storedPackId
            },
            unlockedHeroIds = unlockedHeroes
        )
    }

    suspend fun setGraphicsQuality(value: String) {
        context.appDataStore.edit { it[Keys.graphicsQuality] = normalizeQuality(value) }
    }

    suspend fun setLanguage(value: String) {
        context.appDataStore.edit { it[Keys.selectedLanguage] = normalizeLanguage(value) }
    }

    suspend fun setMusicVolume(value: Float) {
        context.appDataStore.edit { it[Keys.musicVolume] = value }
    }

    suspend fun setSoundVolume(value: Float) {
        context.appDataStore.edit { it[Keys.soundVolume] = value }
    }

    suspend fun setSelectedPackId(value: Int?) {
        context.appDataStore.edit {
            it[Keys.selectedPackId] = value ?: AppDefaults.DefaultSelectedPackId
        }
    }

    suspend fun unlockHero(heroId: Int) {
        context.appDataStore.edit { prefs ->
            val current = prefs[Keys.unlockedHeroIds]
                ?.mapNotNull { it.toIntOrNull() }
                ?.toMutableSet()
                ?: AppDefaults.DefaultUnlockedHeroIds.toMutableSet()
            current.add(AppDefaults.DefaultUnlockedHeroIds.first())
            current.add(heroId)
            prefs[Keys.unlockedHeroIds] = current.map { it.toString() }.toSet()
        }
    }
}


