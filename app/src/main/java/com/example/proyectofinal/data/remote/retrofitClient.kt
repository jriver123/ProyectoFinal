package com.example.proyectofinal.data.remote

import android.util.Log
import com.example.proyectofinal.data.preferences.ConfigManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
	private const val TAG = "RetrofitClient"

	data class ServerProbeResult(
		val reachable: Boolean,
		val testedUrl: String,
		val httpCode: Int? = null,
		val detail: String? = null
	)

	private val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
		Log.d(TAG, message)
	}.apply {
		level = HttpLoggingInterceptor.Level.BODY
	}

	// Cliente HTTP con timeouts y logging
	private val okHttpClient = OkHttpClient.Builder()
		.addInterceptor { chain ->
			val request = chain.request()
			Log.i(TAG, "--> ${request.method} ${request.url}")
			try {
				val response = chain.proceed(request)
				Log.i(TAG, "<-- ${response.code} ${request.url}")
				response
			} catch (e: Exception) {
				Log.e(TAG, "xx> ${request.method} ${request.url} (${e.javaClass.simpleName}: ${e.message})")
				throw e
			}
		}
		.addInterceptor(httpLoggingInterceptor)
		.connectTimeout(30, TimeUnit.SECONDS)
		.readTimeout(30, TimeUnit.SECONDS)
		.writeTimeout(30, TimeUnit.SECONDS)
		.build()

	@Volatile
	private var activeBaseUrl: String? = null

	@Volatile
	private var retrofitInstance: Retrofit? = null

	@Volatile
	private var usuarioApiInstance: UsuarioApi? = null

	@Synchronized
	private fun rebuildIfNeeded(): UsuarioApi {
		val configuredBaseUrl = ConfigManager.getBaseUrl()
		if (retrofitInstance == null || usuarioApiInstance == null || activeBaseUrl != configuredBaseUrl) {
			Log.i(TAG, "Rebuilding Retrofit with baseUrl=$configuredBaseUrl")
			val retrofit = Retrofit.Builder()
				.baseUrl(configuredBaseUrl)
				.client(okHttpClient)
				.addConverterFactory(GsonConverterFactory.create())
				.build()

			retrofitInstance = retrofit
			usuarioApiInstance = retrofit.create(UsuarioApi::class.java)
			activeBaseUrl = configuredBaseUrl
		}

		return usuarioApiInstance!!
	}

	val usuarioApi: UsuarioApi
		get() = rebuildIfNeeded()

	@Synchronized
	fun invalidate() {
		activeBaseUrl = null
		retrofitInstance = null
		usuarioApiInstance = null
	}

	fun probeServer(baseUrl: String = ConfigManager.getBaseUrl()): ServerProbeResult {
		val normalized = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
		val candidates = listOf("${normalized}usuarios", normalized)
		var lastError: Exception? = null

		for (url in candidates) {
			try {
				val request = Request.Builder().url(url).get().build()
				okHttpClient.newCall(request).execute().use { response ->
					return ServerProbeResult(
						reachable = true,
						testedUrl = url,
						httpCode = response.code,
						detail = "Respuesta HTTP ${response.code}"
					)
				}
			} catch (e: Exception) {
				lastError = e
				Log.e(TAG, "Server probe failed for $url: ${e.javaClass.simpleName}: ${e.message}")
			}
		}

		return ServerProbeResult(
			reachable = false,
			testedUrl = candidates.first(),
			detail = lastError?.message ?: "No se pudo conectar al servidor"
		)
	}


}
