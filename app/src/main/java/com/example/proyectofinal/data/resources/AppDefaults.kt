package com.example.proyectofinal.data.resources

object AppDefaults {
    const val PrefsFile = "battle_io_data"

    const val KeyGraphicsQuality = "graphicsQuality"
    const val KeySelectedLanguage = "selectedLanguage"
    const val KeyMusicVolume = "musicVolume"
    const val KeySoundVolume = "soundVolume"
    const val KeySelectedPackId = "selectedPackId"
    const val KeyUnlockedHeroIds = "unlockedHeroIds"

    const val DefaultGraphicsQuality = "high"
    const val DefaultSelectedLanguage = "es"
    const val DefaultMusicVolume = 75f
    const val DefaultSoundVolume = 75f
    const val DefaultSelectedPackId = -1
    val DefaultUnlockedHeroIds = setOf(1)

    val DemoPacks = listOf(
        GamePack(1, "pack_initial", 10, 1000, "bonus_initial"),
        GamePack(2, "pack_pro", 20, 2300, "bonus_pro"),
        GamePack(3, "pack_legendary", 30, 3800, "bonus_legendary")
    )

}

