package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.local.UsuarioLoginDao
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.remote.UsuarioApi
import retrofit2.Response

class UsuarioRepository(
    private val api: UsuarioApi,
    private val usuarioLoginDao: UsuarioLoginDao
) {

  private fun UsuarioUI.toRequest(): UsuarioRequest {
    return UsuarioRequest(
      username = username,
      email = email,
      password = password.ifBlank { null },
      description = description,
      nivel = nivel,
      monedas = monedas,
      partidasGanadas = partidasGanadas,
      partidasJugadas = partidasJugadas,
      storyProgress = storyProgress,
      exp = exp
    )
  }

  private fun requireBody(response: Response<UsuarioUI>, errorMessage: String): UsuarioUI {
    if (response.isSuccessful) {
      return response.body() ?: throw Exception("Usuario no encontrado")
    }
    throw Exception("$errorMessage: ${response.code()}")
  }

  private fun requireLoginBody(response: Response<LoginResponse>): LoginResponse {
    if (response.isSuccessful) {
      return response.body() ?: throw Exception("Respuesta de login vacia")
    }
    throw Exception("Error al iniciar sesion: ${response.code()}")
  }

  private fun errorDetail(responseCode: Int, label: String): String {
    return "$label ($responseCode)"
  }

    // 🔹 Login
    suspend fun login(request: LoginRequest): LoginResponse {
      val normalizedEmail = request.email.trim()
      val normalizedPassword = request.password.trim()

      val primaryResponse = api.login(
        LoginRequest(
          email = normalizedEmail,
          password = normalizedPassword
        )
      )

      if (primaryResponse.isSuccessful) {
        return requireLoginBody(primaryResponse)
      }

      // Fallbacks para backends con nombres de campos distintos en el endpoint /login.
      val fallbackPayloads = listOf(
        mapOf("correo" to normalizedEmail, "password" to normalizedPassword),
        mapOf("correo" to normalizedEmail, "contrasena" to normalizedPassword),
        mapOf("username" to normalizedEmail, "password" to normalizedPassword)
      )

      for (payload in fallbackPayloads) {
        val fallbackResponse = api.loginWithMap(payload)
        if (fallbackResponse.isSuccessful) {
          return requireLoginBody(fallbackResponse)
        }
      }

      throw Exception(errorDetail(primaryResponse.code(), "Credenciales invalidas o contrato de login no compatible"))
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
    val detallesResponse = api.getUsuarioDetalles(id)
    if (detallesResponse.isSuccessful) {
      return detallesResponse.body() ?: throw Exception("Usuario no encontrado")
    }

    if (detallesResponse.code() == 404) {
      val basicResponse = api.getUsuarioById(id)
      return requireBody(basicResponse, "Error al obtener usuario")
    }

    throw Exception("Error al obtener usuario: ${detallesResponse.code()}")
    }

    // 🔹 Registrar usuario
    suspend fun saveUsuario(usuario: UsuarioUI): UsuarioUI {
		val response = api.createUsuario(usuario.toRequest())
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error al registrar usuario")
        } else {
            throw Exception("Error al registrar usuario: ${response.code()}")
        }
    }

    // 🔹 Actualizar usuario
    suspend fun updateUsuario(id: Long, usuario: UsuarioUI): UsuarioUI {
    val response = api.updateUsuario(id, usuario.toRequest())
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error al actualizar usuario")
        } else {
            throw Exception("Error al actualizar usuario: ${response.code()}")
        }
    }

  suspend fun registrarEstadisticasPartida(
    id: Long,
    request: RegistroPartidaRequest
  ): UsuarioUI {
    val response = api.registrarEstadisticasPartida(id, request)
    if (response.isSuccessful) {
      return response.body() ?: throw Exception("No se recibieron estadísticas actualizadas")
    } else {
      throw Exception("Error al registrar estadísticas: ${response.code()}")
    }
  }

    // 🔹 Eliminar usuario
    suspend fun deleteUsuario(id: Long): Boolean {
        val response = api.deleteUsuario(id)
        return response.isSuccessful
    }
}
