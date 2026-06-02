package com.example.proyectofinal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.data.model.Usuario
import com.example.proyectofinal.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UsuarioUiState(
	val usuarios: List<Usuario> = emptyList(),
	val usuarioActivo: Usuario? = null,
	val isLoading: Boolean = false,
	val message: String? = null,
	val errorMessage: String? = null
)

class UsuarioViewModel(
	private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

	private val _uiState = MutableStateFlow(UsuarioUiState())
	val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

	init {
		refreshUsuarios()
	}

	fun refreshUsuarios() {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, message = null)
			runCatching {
				repository.getAllUsuarios()
			}.onSuccess { usuarios ->
				_uiState.value = _uiState.value.copy(
					usuarios = usuarios,
					usuarioActivo = usuarios.firstOrNull(),
					isLoading = false,
					message = if (usuarios.isEmpty()) "No hay usuarios en la API" else "Usuarios cargados"
				)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					errorMessage = throwable.message ?: "Error al cargar usuarios"
				)
			}
		}
	}

	fun saveUsuario(usuario: Usuario, onResult: (Usuario?) -> Unit = {}) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, message = null)
			runCatching {
				repository.saveUsuario(usuario)
			}.onSuccess { saved ->
				refreshUsuarios()
				onResult(saved)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					errorMessage = throwable.message ?: "Error al guardar usuario"
				)
				onResult(null)
			}
		}
	}

	fun deleteUsuario(id: Long, onResult: (Boolean) -> Unit = {}) {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, message = null)
			runCatching {
				repository.deleteUsuario(id)
			}.onSuccess {
				refreshUsuarios()
				onResult(true)
			}.onFailure { throwable ->
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					errorMessage = throwable.message ?: "Error al eliminar usuario"
				)
				onResult(false)
			}
		}
	}

	fun selectUsuario(usuario: Usuario?) {
		_uiState.value = _uiState.value.copy(usuarioActivo = usuario)
	}
}
