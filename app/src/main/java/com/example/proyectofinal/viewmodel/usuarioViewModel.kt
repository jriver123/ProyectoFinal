package com.example.proyectofinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.model.*
import com.example.proyectofinal.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
			is IOException -> "Error de conexión"
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
					nivel = 0,
					monedas = 0,
					partidasGanadas = 0,
					partidasJugadas = 0,
					storyProgress = 0,
					exp = 0,
					password = normalizedPassword
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

	// 🔹 Actualizar usuario
	fun actualizarUsuario(id: Long, usuario: UsuarioUI, onResult: (UsuarioUI?) -> Unit = {}) {
		viewModelScope.launch {
			beginRequest()
			runCatching<UsuarioUI> {
				repository.updateUsuario(id, usuario)
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
				// Llamada al repositorio para actualizar el progreso
				val usuarioActualizado = repository.updateUsuario(
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

	fun registrarResultadoPartida(
		victoria: Boolean,
		monedasGanadas: Int,
		xpGanada: Int
	) {
		val usuarioActual = _uiState.value.usuarioActivo ?: return

		viewModelScope.launch {
			beginRequest()
			runCatching {
				repository.registrarEstadisticasPartida(
					id = usuarioActual.id,
					request = RegistroPartidaRequest(
						monedasGanadas = monedasGanadas,
						victorias = if (victoria) 1 else 0,
						partidasJugadas = 1,
						expGanada = xpGanada
					)
				)
			}.onSuccess { usuarioActualizado ->
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
			}.onFailure { throwable ->
				failRequest(throwable)
			}
		}
	}

}
