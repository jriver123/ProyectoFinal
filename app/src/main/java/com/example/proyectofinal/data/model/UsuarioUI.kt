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
    @SerializedName(value = "winned_matches", alternate = ["winnedMatches"])
    val partidasGanadas: Int = 0,
    @SerializedName(value = "played_matches", alternate = ["playedMatches"])
    val partidasJugadas: Int = 0,
    @SerializedName(value = "story_progress", alternate = ["storyProgress"])
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
    @SerializedName(value = "winned_matches", alternate = ["winnedMatches"])
    val partidasGanadas: Int = 0,
    @SerializedName(value = "played_matches", alternate = ["playedMatches"])
    val partidasJugadas: Int = 0,
    @SerializedName(value = "story_progress", alternate = ["storyProgress"])
    val storyProgress: Int = 0,
    @SerializedName("exp")
    val exp: Int = 0
)

data class RegistroPartidaRequest(
    val monedasGanadas: Int = 0,
    val victorias: Int = 0,
    val partidasJugadas: Int = 1,
    val expGanada: Int = 0
)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: Long, val token: String,val nombre: String, val correo: String)
