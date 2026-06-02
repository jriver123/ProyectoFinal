package com.example.proyectofinal.data.model

data class Usuario(
	val id: Long? = null,
	val username: String,
	val password: String,
	val description: String,
	val email: String
)
