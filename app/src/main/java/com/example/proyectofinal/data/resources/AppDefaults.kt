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

    val DemoMatches = listOf(
        MatchHistory(1, "match_quick", "result_win", "+120 XP"),
        MatchHistory(2, "match_survival", "result_loss", "+40 XP"),
        MatchHistory(3, "match_competitive", "result_win", "+180 XP"),
        MatchHistory(4, "match_weekly", "result_win", "+250 XP")
    )
}

