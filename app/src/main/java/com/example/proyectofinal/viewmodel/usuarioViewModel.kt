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

	// 🔹 Login
	fun login(email: String, password: String, onResult: (UsuarioUI?) -> Unit = {}) {
		viewModelScope.launch {
			runCatching<LoginResponse> {
				repository.login(LoginRequest(email, password))
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
					password = password
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

				_uiState.value = _uiState.value.copy(usuarioActivo = usuarioUI)
				onResult(usuarioUI)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = handleError(throwable))
				onResult(null)
			}
		}
	}

	// 🔹 Cargar detalles del usuario activo
	fun cargarUsuarioDetalles(id: Long) {
		viewModelScope.launch {
			runCatching<UsuarioUI> {
				repository.getUsuarioDetalles(id)
			}.onSuccess { usuario ->
				_uiState.value = _uiState.value.copy(usuarioActivo = usuario)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = handleError(throwable))
			}
		}
	}

	// 🔹 Registrar usuario
	fun registrarUsuario(usuario: UsuarioUI, onResult: (UsuarioUI?) -> Unit = {}) {
		viewModelScope.launch {
			runCatching<UsuarioUI> {
				repository.saveUsuario(usuario)
			}.onSuccess { saved ->
				onResult(saved)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = handleError(throwable))
				onResult(null)
			}
		}
	}

	// 🔹 Actualizar usuario
	fun actualizarUsuario(id: Long, usuario: UsuarioUI, onResult: (UsuarioUI?) -> Unit = {}) {
		viewModelScope.launch {
			runCatching<UsuarioUI> {
				repository.updateUsuario(id, usuario)
			}.onSuccess { updated ->
				onResult(updated)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = handleError(throwable))
				onResult(null)
			}
		}
	}

	// 🔹 Eliminar usuario
	fun eliminarUsuario(id: Long, onResult: (Boolean) -> Unit = {}) {
		viewModelScope.launch {
			runCatching<Boolean> {
				repository.deleteUsuario(id)
			}.onSuccess {
				onResult(true)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(errorMessage = handleError(throwable))
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
			_uiState.value = UsuarioUiState(usuarioActivo = null)
			onResult()
		}
	}
	fun resetStory(userId: Long) {
		viewModelScope.launch {
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
						password = "" // no se envía
					)
				)
				_uiState.value = _uiState.value.copy(usuarioActivo = usuarioActualizado)
			}.onFailure {
				_uiState.value = _uiState.value.copy(errorMessage = "Error al reiniciar historia")
			}
		}
	}

}
