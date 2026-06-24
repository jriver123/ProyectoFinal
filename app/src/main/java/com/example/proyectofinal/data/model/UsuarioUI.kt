package com.example.proyectofinal.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioUI(
    val id: Long,
    val username: String,
    val email: String,
    val description: String? = null,
    @SerializedName("level")
    val nivel: Int = 0,
    @SerializedName("coins")
    val monedas: Int = 0,
    @SerializedName("winned_matches")
    val partidasGanadas: Int = 0,
    @SerializedName("played_matches")
    val partidasJugadas: Int = 0,
    @SerializedName(value = "storyProgress", alternate = ["story_progress"])
    val storyProgress: Int = 0,
    @SerializedName("exp")
    val exp: Int = 0,
    val password: String = "" // ✅ opcional, pero disponible cuando se necesite
)

data class UsuarioRequest(
    val username: String,
    val email: String,
    val password: String? = null,
    val description: String? = null,
    @SerializedName("level")
    val nivel: Int = 0,
    @SerializedName("coins")
    val monedas: Int = 0,
    @SerializedName("winned_matches")
    val partidasGanadas: Int = 0,
    @SerializedName("played_matches")
    val partidasJugadas: Int = 0,
    @SerializedName(value = "storyProgress", alternate = ["story_progress"])
    val storyProgress: Int = 0,
    @SerializedName("exp")
    val exp: Int = 0
)

// ✅ Para actualizaciones: NO incluir password para evitar corrupción en servidor
data class ActualizarParRequest(
    val username: String,
    val email: String,
    val description: String? = null
)

data class ActualizarUsuarioRequest(
    val username: String,
    val email: String,
    val description: String? = null,
    val password: String
)

data class ActualizarStatsRequest(
    @SerializedName("level")
    val nivel: Int = 0,
    @SerializedName("coins")
    val monedas: Int = 0,
    @SerializedName("winned_matches")
    val partidasGanadas: Int = 0,
    @SerializedName("played_matches")
    val partidasJugadas: Int = 0,
    @SerializedName(value = "storyProgress", alternate = ["story_progress"])
    val storyProgress: Int = 0
)

data class RegistroPartidaRequest(
    @SerializedName("coins_earned")
    val monedasGanadas: Int = 0,
    @SerializedName(value = "winned_matches", alternate = ["wins", "victories"])
    val victorias: Int = 0,
    @SerializedName(value = "played_matches", alternate = ["matches_played"])
    val partidasJugadas: Int = 1,
    @SerializedName("exp_earned")
    val expGanada: Int = 0
)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: Long, val token: String,val nombre: String, val correo: String)
