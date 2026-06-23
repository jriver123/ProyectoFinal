package com.example.proyectofinal.data.repository

import com.example.proyectofinal.data.local.HeroProgressDao
import com.example.proyectofinal.data.local.UsuarioLoginDao
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.remote.UsuarioApi
import com.example.proyectofinal.data.resources.Hero
import retrofit2.Response
import com.example.proyectofinal.data.resources.calcularXpRequerida

class UsuarioRepository(
    private val api: UsuarioApi,
    private val usuarioLoginDao: UsuarioLoginDao,
    private val heroProgressDao: HeroProgressDao
) {

  private fun HeroProgressEntity.toHero(baseHero: Hero): Hero {
    val parsedAttacks = if (attacksCsv.isBlank()) {
      baseHero.attacks
    } else {
      attacksCsv.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    return baseHero.apply {
      level = this@toHero.level
      currentXP = this@toHero.currentXP
      nextLevelXP = this@toHero.nextLevelXP
      HpStat = this@toHero.hpStat
      attackStat = this@toHero.attackStat
      defenseStat = this@toHero.defenseStat
      luckStat = this@toHero.luckStat
      attacks = parsedAttacks
    }
  }

  private fun Hero.toEntity(userId: Long): HeroProgressEntity {
    return HeroProgressEntity(
      userId = userId,
      heroId = id,
      level = level,
      currentXP = currentXP,
      nextLevelXP = nextLevelXP,
      hpStat = HpStat,
      attackStat = attackStat,
      defenseStat = defenseStat,
      luckStat = luckStat,
      attacksCsv = attacks.joinToString(",")
    )
  }

  suspend fun loadHeroProgress(userId: Long, baseHeroes: Collection<Hero>): List<Hero> {
    val savedProgress = heroProgressDao.getByUser(userId)
    if (savedProgress.isEmpty()) {
      saveHeroProgress(userId, baseHeroes)
      return baseHeroes.toList()
    }

    val savedById = savedProgress.associateBy { it.heroId }
    return baseHeroes.map { hero ->
      val saved = savedById[hero.id]
      if (saved != null) saved.toHero(hero) else hero
    }
  }

  suspend fun saveHeroProgress(userId: Long, heroes: Collection<Hero>) {
    val entities = heroes.map { it.toEntity(userId) }
    heroProgressDao.upsertAll(entities)
  }

  suspend fun saveHeroProgress(userId: Long, hero: Hero) {
    heroProgressDao.upsert(hero.toEntity(userId))
  }

  private fun UsuarioUI.toRequest(): UsuarioRequest {
    return UsuarioRequest(
      username = username,
      email = email,
      password = password.ifBlank { null },
      description = description,
      nivel = nivel.coerceAtLeast(1),
      monedas = monedas.coerceAtLeast(0),
      partidasGanadas = partidasGanadas.coerceAtLeast(0),
      partidasJugadas = partidasJugadas.coerceAtLeast(0),
      storyProgress = storyProgress.coerceAtLeast(1),
      exp = exp.coerceAtLeast(0)
    )
  }

   // ✅ Para actualizaciones: no enviar password para evitar afectar credenciales
   private fun UsuarioUI.toUpdateRequest(): UsuarioUpdateRequest {
     return UsuarioUpdateRequest(
       username = username,
       email = email,
       description = description,
       nivel = nivel.coerceAtLeast(1),
       monedas = monedas.coerceAtLeast(0),
       partidasGanadas = partidasGanadas.coerceAtLeast(0),
       partidasJugadas = partidasJugadas.coerceAtLeast(0),
       storyProgress = storyProgress.coerceAtLeast(1),
       exp = exp.coerceAtLeast(0)
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
    }
    throw Exception("Error al registrar usuario: ${response.code()}")
  }

  // Actualizar usuario
  suspend fun updateUsuario(id: Long, usuario: UsuarioUI): UsuarioUI {
    val response = api.updateUsuario(id, usuario.toUpdateRequest())
    if (response.isSuccessful) {
      return response.body() ?: throw Exception("Error al actualizar usuario")
    }
    throw Exception("Error al actualizar usuario: ${response.code()}")
  }

  suspend fun registrarEstadisticasPartida(
    id: Long,
    request: RegistroPartidaRequest
  ): UsuarioUI {
    println("🔵 DEBUG: Registrando estadísticas para usuario $id")
    println("🔵 DEBUG: Request = $request")
    val response = api.registrarEstadisticasPartida(id, request)
    println("🔵 DEBUG: Response code = ${response.code()}")
    println("🔵 DEBUG: Response body = ${response.body()}")
    if (response.isSuccessful) {
      return response.body() ?: throw Exception("No se recibieron estadísticas actualizadas")
    } else {
      val errorBody = response.errorBody()?.string()
      println("🔵 DEBUG: Error body = $errorBody")
      throw Exception("Error al registrar estadísticas: ${response.code()} - $errorBody")
    }
  }

 private fun calcularNivelUsuario(expTotal: Int): Int {
    var nivel = 1
    var xpRestante = expTotal
    while (nivel < 100) {
        val xpNecesaria = calcularXpRequerida(nivel)
        if (xpRestante < xpNecesaria) break
        xpRestante -= xpNecesaria
        nivel++
    }
    return nivel
}

suspend fun actualizarEstadisticasUsuario(
    id: Long,
    victoria: Boolean,
    monedasGanadas: Int,
    xpGanada: Int,
    usuarioActual: UsuarioUI
): UsuarioUI {
    val nuevaExp = (usuarioActual.exp + xpGanada).coerceAtLeast(0)
    val nuevoNivel = calcularNivelUsuario(nuevaExp).coerceAtLeast(1)

    val usuarioActualizado = usuarioActual.copy(
        monedas = (usuarioActual.monedas + monedasGanadas).coerceAtLeast(0),
        exp = nuevaExp,
        nivel = nuevoNivel,
        partidasJugadas = (usuarioActual.partidasJugadas + 1).coerceAtLeast(0),
        partidasGanadas = (usuarioActual.partidasGanadas + if (victoria) 1 else 0).coerceAtLeast(0),
        storyProgress = usuarioActual.storyProgress.coerceAtLeast(1)
    )
    return updateUsuario(id, usuarioActualizado)
}

    // 🔹 Eliminar usuario
    suspend fun deleteUsuario(id: Long): Boolean {
        val response = api.deleteUsuario(id)
        return response.isSuccessful
    }
}
