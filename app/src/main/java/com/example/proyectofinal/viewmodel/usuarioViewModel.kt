package com.example.proyectofinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException

data class UsuarioUiState(
	val usuarios: List<UsuarioUI> = emptyList(),
	val usuarioActivo: UsuarioUI? = null,
	val isLoading: Boolean = false,
	val message: String? = null,
	val errorMessage: String? = null
)

class UsuarioViewModel(
	private val repository: UsuarioRepository
) : ViewModel() {
	private val _uiState = MutableStateFlow(UsuarioUiState())
	val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

	// 🔹 Manejo centralizado de errores
	private fun handleError(e: Throwable): String {
		return when (e) {
			is HttpException -> "Error HTTP ${e.code()}"
			is UnknownHostException -> "No se pudo resolver el host. Revisa IP/URL del servidor."
			is ConnectException -> "No se pudo conectar al servidor. Revisa red, IP y puerto."
			is SocketTimeoutException -> "Tiempo de espera agotado al conectar con la API."
			is IOException -> "Error de conexión: ${e.localizedMessage ?: "sin detalle"}"
			else -> "Error inesperado: ${e.localizedMessage}"
		}
	}

	private fun beginRequest() {
		_uiState.value = _uiState.value.copy(
			isLoading = true,
			message = null,
			errorMessage = null
		)
	}

	private fun failRequest(throwable: Throwable) {
		_uiState.value = _uiState.value.copy(
			isLoading = false,
			message = null,
			errorMessage = handleError(throwable)
		)
	}

	fun clearFeedback() {
		_uiState.value = _uiState.value.copy(message = null, errorMessage = null)
	}

	// 🔹 Login
	fun login(email: String, password: String, onResult: (UsuarioUI?) -> Unit = {}) {
		val normalizedEmail = email.trim()
		val normalizedPassword = password.trim()
		if (normalizedEmail.isBlank() || normalizedPassword.isBlank()) {
			_uiState.value = _uiState.value.copy(
				isLoading = false,
				message = null,
				errorMessage = "Ingresa correo y contraseña"
			)
			onResult(null)
			return
		}

		viewModelScope.launch {
			beginRequest()
			runCatching<LoginResponse> {
				repository.login(LoginRequest(normalizedEmail, normalizedPassword))
			}.onSuccess { response ->
				val usuarioUI = UsuarioUI(
					id = response.id,
					username = response.nombre,
					email = response.correo,
					description = "",
					nivel = 1,
					monedas = 0,
					partidasGanadas = 0,
					partidasJugadas = 0,
					storyProgress = 1,
					exp = 0
					// ✅ No guardar password en el objeto de UI - se almacena en Room
				)

				// Guardar en Room
				repository.guardarUsuarioLocal(
					UsuarioLoginEntity(
						id = response.id,
						nombre = response.nombre,
						correo = response.correo,
						token = response.token
					)
				)

				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuarioUI,
					message = "Inicio de sesión exitoso",
					errorMessage = null
				)
				onResult(usuarioUI)
			}.onFailure { throwable ->
				failRequest(throwable)
				onResult(null)
			}
		}
	}

	// 🔹 Cargar detalles del usuario activo
	fun cargarUsuarioDetalles(id: Long) {
		viewModelScope.launch {
			beginRequest()
			runCatching<UsuarioUI> {
				repository.getUsuarioDetalles(id)
			}.onSuccess { usuario ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuario,
					message = "Datos de usuario actualizados",
					errorMessage = null
				)
			}.onFailure { throwable ->
				failRequest(throwable)
			}
		}
	}

	// 🔹 Registrar usuario
	fun registrarUsuario(usuario: UsuarioUI, onResult: (UsuarioUI?) -> Unit = {}) {
		viewModelScope.launch {
			beginRequest()
			runCatching<UsuarioUI> {
				repository.saveUsuario(usuario)
			}.onSuccess { saved ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					message = "Usuario registrado correctamente",
					errorMessage = null
				)
				onResult(saved)
			}.onFailure { throwable ->
				failRequest(throwable)
				onResult(null)
			}
		}
	}

	fun registrarNuevoUsuario(
		username: String,
		email: String,
		password: String,
		description: String,
		onResult: (UsuarioUI?) -> Unit = {}
	) {
		val nuevoUsuario = UsuarioUI(
			id = 0,
			username = username.trim(),
			email = email.trim(),
			description = description.trim(),
			nivel = 1,
			monedas = 100,
			partidasGanadas = 0,
			partidasJugadas = 0,
			storyProgress = 1,
			exp = 0,
			password = password.trim()
		)

		registrarUsuario(nuevoUsuario, onResult)
	}

	// 🔹 Actualizar usuario
	// Si passwordChanged=false, se forza update parcial (sin password).
	fun actualizarUsuario(
		id: Long,
		usuario: UsuarioUI,
		passwordChanged: Boolean = false,
		onResult: (UsuarioUI?) -> Unit = {}
	) {
		viewModelScope.launch {
			beginRequest()
			runCatching<UsuarioUI> {
				val payload = if (passwordChanged) {
					usuario
				} else {
					usuario.copy(password = "")
				}
				repository.updateUsuario(id, payload)
			}.onSuccess { updated ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = updated,
					message = "Usuario actualizado correctamente",
					errorMessage = null
				)
				onResult(updated)
			}.onFailure { throwable ->
				failRequest(throwable)
				onResult(null)
			}
		}
	}

	// 🔹 Eliminar usuario
	fun eliminarUsuario(id: Long, onResult: (Boolean) -> Unit = {}) {
		viewModelScope.launch {
			beginRequest()
			runCatching<Boolean> {
				repository.deleteUsuario(id)
			}.onSuccess { deleted ->
				_uiState.value = if (deleted) {
					_uiState.value.copy(
						isLoading = false,
						usuarioActivo = null,
						message = "Usuario eliminado correctamente",
						errorMessage = null
					)
				} else {
					_uiState.value.copy(
						isLoading = false,
						message = null,
						errorMessage = "No se pudo eliminar el usuario"
					)
				}
				onResult(deleted)
			}.onFailure { throwable ->
				failRequest(throwable)
				onResult(false)
			}
		}
	}

	// 🔹 Verificar login automático
	fun verificarLoginAutomatico(onResult: (UsuarioLoginEntity?) -> Unit) {
		viewModelScope.launch {
			val usuarioGuardado = repository.getUsuarioGuardado()
			if (usuarioGuardado != null) {
				cargarUsuarioDetalles(usuarioGuardado.id)
			}
			onResult(usuarioGuardado)
		}
	}

	// 🔹 Logout
	fun logout(onResult: () -> Unit = {}) {
		viewModelScope.launch {
			repository.logout()
			_uiState.value = UsuarioUiState(
				usuarioActivo = null,
				message = "Sesión cerrada"
			)
			onResult()
		}
	}
	fun resetStory(userId: Long) {
		viewModelScope.launch {
			beginRequest()
			runCatching {
				// Solo stats para progreso de historia
				val usuarioActualizado = repository.updateUserStatsFields(
					userId,
					UsuarioUI(
						id = userId,
						username = uiState.value.usuarioActivo?.username ?: "",
						email = uiState.value.usuarioActivo?.email ?: "",
						description = uiState.value.usuarioActivo?.description,
						nivel = uiState.value.usuarioActivo?.nivel ?: 0,
						monedas = uiState.value.usuarioActivo?.monedas ?: 0,
						partidasGanadas = uiState.value.usuarioActivo?.partidasGanadas ?: 0,
						partidasJugadas = uiState.value.usuarioActivo?.partidasJugadas ?: 0,
						storyProgress = 1, // ✅ reinicia al mínimo
									exp = uiState.value.usuarioActivo?.exp ?: 0,
						password = "" // no se envía
					)
				)
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuarioActualizado,
					message = "Historia reiniciada",
					errorMessage = null
				)
			}.onFailure { throwable ->
				failRequest(throwable)
			}
		}
	}

	fun advanceStory(userId: Long, maxChapters: Int = 3) {
		viewModelScope.launch {
			beginRequest()
			runCatching {
				val usuarioActual = uiState.value.usuarioActivo ?: return@runCatching
				val nextProgress = (usuarioActual.storyProgress + 1).coerceAtMost(maxChapters)
				val usuarioActualizado = repository.updateUserStatsFields(
					userId,
					UsuarioUI(
						id = userId,
						username = usuarioActual.username,
						email = usuarioActual.email,
						description = usuarioActual.description,
						nivel = usuarioActual.nivel,
						monedas = usuarioActual.monedas,
						partidasGanadas = usuarioActual.partidasGanadas,
						partidasJugadas = usuarioActual.partidasJugadas,
						storyProgress = nextProgress,
						exp = usuarioActual.exp,
						password = ""
					)
				)
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuarioActualizado,
					message = null,
					errorMessage = null
				)
			}.onFailure { throwable ->
				failRequest(throwable)
			}
		}
	}

	fun comprarHeroe(heroId: Int, costoMonedas: Int, onResult: (Boolean) -> Unit = {}) {
		val usuarioActual = _uiState.value.usuarioActivo
		if (usuarioActual == null) {
			_uiState.value = _uiState.value.copy(
				isLoading = false,
				message = null,
				errorMessage = "No hay usuario activo"
			)
			onResult(false)
			return
		}

		if (usuarioActual.monedas < costoMonedas) {
			_uiState.value = _uiState.value.copy(
				isLoading = false,
				message = null,
				errorMessage = "No tienes suficientes monedas"
			)
			onResult(false)
			return
		}

		viewModelScope.launch {
			beginRequest()
			runCatching {
				val usuarioActualizado = repository.updateUserStatsFields(
					usuarioActual.id,
					usuarioActual.copy(monedas = (usuarioActual.monedas - costoMonedas).coerceAtLeast(0))
				)
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuarioActualizado,
					message = "Heroe desbloqueado correctamente (#$heroId)",
					errorMessage = null
				)
			}.onSuccess {
				onResult(true)
			}.onFailure { throwable ->
				failRequest(throwable)
				onResult(false)
			}
		}
	}

	fun registrarResultadoPartida(
		victoria: Boolean,
		monedasGanadas: Int,
		xpGanada: Int,
		onResult: (Boolean) -> Unit = {}
	) {
		val usuarioActual = _uiState.value.usuarioActivo ?: return

		viewModelScope.launch {
			beginRequest()
			runCatching {
				println("🔵 DEBUG: Registrando partida")
				println("🔵 DEBUG: Victoria = $victoria")
				println("🔵 DEBUG: Usuario actual: victorias=${usuarioActual.partidasGanadas}, partidas=${usuarioActual.partidasJugadas}, monedas=${usuarioActual.monedas}, exp=${usuarioActual.exp}")

				// Usar el método alternativo que actualiza el usuario completo
				repository.actualizarEstadisticasUsuario(
					id = usuarioActual.id,
					victoria = victoria,
					monedasGanadas = monedasGanadas,
					xpGanada = xpGanada,
					usuarioActual = usuarioActual.copy(
						nivel = usuarioActual.nivel.coerceAtLeast(1),
						storyProgress = usuarioActual.storyProgress.coerceAtLeast(1)
					)
				)
			}.onSuccess { usuarioActualizado ->
				println("🔵 DEBUG: Estadísticas registradas exitosamente")
				println("🔵 DEBUG: Usuario actualizado: victorias=${usuarioActualizado.partidasGanadas}, partidas=${usuarioActualizado.partidasJugadas}, monedas=${usuarioActualizado.monedas}, exp=${usuarioActualizado.exp}")
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					usuarioActivo = usuarioActualizado,
					message = if (victoria) {
						"Victoria registrada: +$monedasGanadas monedas, +$xpGanada XP"
					} else {
						"Partida registrada: +$monedasGanadas monedas, +$xpGanada XP"
					},
					errorMessage = null
				)
				// Recargar datos del usuario del servidor para sincronizar
				viewModelScope.launch {
					delay(500)
					cargarUsuarioDetalles(usuarioActual.id)
				}
				onResult(true)
			}.onFailure { throwable ->
				println("🔵 DEBUG: Error al registrar estadísticas: ${throwable.message}")
				throwable.printStackTrace()
				failRequest(throwable)
				onResult(false)
			}
		}
	}

}
