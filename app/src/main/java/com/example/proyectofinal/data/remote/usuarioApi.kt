package com.example.proyectofinal.data.remote

import com.example.proyectofinal.data.model.LoginRequest
import com.example.proyectofinal.data.model.LoginResponse
import com.example.proyectofinal.data.model.ActualizarParRequest
import com.example.proyectofinal.data.model.ActualizarStatsRequest
import com.example.proyectofinal.data.model.ActualizarUsuarioRequest
import com.example.proyectofinal.data.model.UsuarioRequest
import com.example.proyectofinal.data.model.UsuarioUI
import retrofit2.Response
import retrofit2.http.*


interface UsuarioApi {
	// Login
	@POST("usuarios/login")
	suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

	@POST("usuarios/login")
	suspend fun loginWithMap(@Body request: Map<String, String>): Response<LoginResponse>

	// CRUD de usuarios
	@GET("usuarios")
	suspend fun getAllUsuarios(): Response<List<UsuarioUI>>

	@GET("usuarios/{id}")
	suspend fun getUsuarioById(@Path("id") id: Long): Response<UsuarioUI>

	@POST("usuarios")
	suspend fun createUsuario(@Body usuario: UsuarioRequest): Response<UsuarioUI>

	@PUT("usuarios/{id}/actualizar-par")
	suspend fun updatePerfilParcial(@Path("id") id: Long, @Body request: ActualizarParRequest): Response<UsuarioUI>

	@PUT("usuarios/{id}/actualizar-usuario")
	suspend fun updatePerfilCompleto(@Path("id") id: Long, @Body request: ActualizarUsuarioRequest): Response<UsuarioUI>

	@PUT("usuarios/{id}/actualizar-stats")
	suspend fun updateStats(@Path("id") id: Long, @Body request: ActualizarStatsRequest): Response<UsuarioUI>

	@DELETE("usuarios/{id}")
	suspend fun deleteUsuario(@Path("id") id: Long): Response<Unit>

	// Datos combinados (DTO)
	@GET("usuarios/{id}/detalles")
	suspend fun getUsuarioDetalles(@Path("id") id: Long): Response<UsuarioUI>
}
