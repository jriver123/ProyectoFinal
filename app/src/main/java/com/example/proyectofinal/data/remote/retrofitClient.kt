package com.example.proyectofinal.data.remote

import com.example.proyectofinal.data.preferences.ConfigManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {


	// Interceptor para ver las peticiones/respuestas en Logcat


	// Cliente HTTP con timeouts y logging
	private val okHttpClient = OkHttpClient.Builder()
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
		.build()

	// Retrofit configurado con Gson
	val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(ConfigManager.getBaseUrl())
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	val usuarioApi: UsuarioApi by lazy {
		retrofit.create(UsuarioApi::class.java)
	}


}
