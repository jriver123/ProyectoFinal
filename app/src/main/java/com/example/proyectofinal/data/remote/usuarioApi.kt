package com.example.proyectofinal.data.remote

import com.example.proyectofinal.data.model.LoginRequest
import com.example.proyectofinal.data.model.LoginResponse
import com.example.proyectofinal.data.model.UsuarioUI
import retrofit2.Response
import retrofit2.http.*


interface UsuarioApi {
	// Login
	@POST("usuarios/login")
	suspend fun login(@Body request: LoginRequest): LoginResponse

	// CRUD de usuarios
	@GET("usuarios")
	suspend fun getAllUsuarios(): Response<List<UsuarioUI>>

	@GET("usuarios/{id}")
	suspend fun getUsuarioById(@Path("id") id: Long): Response<UsuarioUI>

	@POST("usuarios")
	suspend fun createUsuario(@Body usuario: UsuarioUI): Response<UsuarioUI>

	@PUT("usuarios/{id}")
	suspend fun updateUsuario(@Path("id") id: Long, @Body usuario: UsuarioUI): Response<UsuarioUI>

	@DELETE("usuarios/{id}")
	suspend fun deleteUsuario(@Path("id") id: Long): Response<Unit>

	// Datos combinados (DTO)
	@GET("usuarios/{id}/detalles")
	suspend fun getUsuarioDetalles(@Path("id") id: Long): Response<UsuarioUI>
}
