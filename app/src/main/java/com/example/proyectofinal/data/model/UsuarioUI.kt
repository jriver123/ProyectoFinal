package com.example.proyectofinal.data.model

data class UsuarioUI(
    val id: Long,
    val username: String,
    val email: String,
    val description: String? = null,
    val nivel: Int = 0,
    val monedas: Int = 0,
    val partidasGanadas: Int = 0,
    val partidasJugadas: Int = 0,
    val storyProgress: Int = 0,
    val password: String = "" // ✅ opcional, pero disponible cuando se necesite
)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: Long, val token: String,val nombre: String, val correo: String)
