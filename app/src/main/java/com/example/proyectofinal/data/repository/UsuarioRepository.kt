package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.local.UsuarioLoginDao
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.remote.UsuarioApi

class UsuarioRepository(
    private val api: UsuarioApi,
    private val usuarioLoginDao: UsuarioLoginDao
) {

    // 🔹 Login
    suspend fun login(request: LoginRequest): LoginResponse {
        return api.login(request)
    }

    // 🔹 Guardar usuario en Room
    suspend fun guardarUsuarioLocal(usuario: UsuarioLoginEntity) {
        usuarioLoginDao.insert(usuario)
    }

    suspend fun getUsuarioGuardado(): UsuarioLoginEntity? {
        return usuarioLoginDao.getUsuario()
    }

    suspend fun logout() {
        usuarioLoginDao.clear()
    }

    // 🔹 Obtener detalles del usuario desde la API
    suspend fun getUsuarioDetalles(id: Long): UsuarioUI {
        val response = api.getUsuarioById(id)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Usuario no encontrado")
        } else {
            throw Exception("Error al obtener usuario: ${response.code()}")
        }
    }

    // 🔹 Registrar usuario
    suspend fun saveUsuario(usuario: UsuarioUI): UsuarioUI {
        val response = api.createUsuario(usuario)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error al registrar usuario")
        } else {
            throw Exception("Error al registrar usuario: ${response.code()}")
        }
    }

    // 🔹 Actualizar usuario
    suspend fun updateUsuario(id: Long, usuario: UsuarioUI): UsuarioUI {
        val response = api.updateUsuario(id, usuario)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error al actualizar usuario")
        } else {
            throw Exception("Error al actualizar usuario: ${response.code()}")
        }
    }

    // 🔹 Eliminar usuario
    suspend fun deleteUsuario(id: Long): Boolean {
        val response = api.deleteUsuario(id)
        return response.isSuccessful
    }
}

