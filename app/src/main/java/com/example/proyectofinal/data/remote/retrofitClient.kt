package com.example.proyectofinal.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
	private const val BASE_URL = "http://10.26.254.205:8080/api/"

	private val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
			.build()
	}

	val usuarioApi: UsuarioApi by lazy {
		retrofit.create(UsuarioApi::class.java)
	}
}