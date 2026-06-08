package com.example.proyectofinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario_login")
data class UsuarioLoginEntity(
    @PrimaryKey val id: Long,
    val nombre: String,
    val correo: String,
    val token: String? = null // opcional si usas JWT
)
