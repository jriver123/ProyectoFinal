package com.example.proyectofinal.data.remote

import com.example.proyectofinal.data.model.Usuario
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {
	@GET("usuarios")
	suspend fun getAllUsuarios(): List<Usuario>

	@GET("usuarios/{id}")
	suspend fun getUsuarioById(@Path("id") id: Long): Usuario

	@POST("usuarios")
	suspend fun createUsuario(@Body usuario: Usuario): Usuario

	@PUT("usuarios/{id}")
	suspend fun updateUsuario(
		@Path("id") id: Long,
		@Body usuario: Usuario
	): Usuario

	@DELETE("usuarios/{id}")
	suspend fun deleteUsuario(@Path("id") id: Long)
}