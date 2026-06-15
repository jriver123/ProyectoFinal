package com.example.proyectofinal.ui.navigation

object AppRoutes {
    const val Login = "login"
    const val Home = "home"
    const val Matches = "matches"
    const val Store = "store"
    const val Profile = "profile"
    const val Settings = "settings"
    const val Story = "story"
    const val CharacterSelect = "character_select"

    const val BattleBase = "battle"
    const val BattleWithArg = "$BattleBase/{characterId}"

    fun battle(characterId: Int): String = "$BattleBase/$characterId"
}

