package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.model.Usuario
import com.example.proyectofinal.data.remote.RetrofitClient

class UsuarioRepository(
    private val api: com.example.proyectofinal.data.remote.UsuarioApi = RetrofitClient.usuarioApi
) {
    suspend fun getAllUsuarios(): List<Usuario> {
        return api.getAllUsuarios()
    }

    suspend fun getUsuarioById(id: Long): Usuario? {
        return runCatching {
            api.getUsuarioById(id)
        }.getOrNull()
    }

    suspend fun saveUsuario(usuario: Usuario): Usuario {
        return if (usuario.id == null) {
            api.createUsuario(usuario)
        } else {
            api.updateUsuario(usuario.id, usuario)
        }
    }

    suspend fun deleteUsuario(id: Long) {
        api.deleteUsuario(id)
    }

    suspend fun getPrimaryUsuario(): Usuario? {
        return getAllUsuarios().firstOrNull()
    }

    fun isNetworkError(error: Throwable): Boolean {
        return error is java.io.IOException
    }
}

