package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectofinal.data.preferences.ConfigManager
import com.example.proyectofinal.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ServerConfigScreen(navController: NavController) {
    val baseUrlState = remember { mutableStateOf(ConfigManager.getBaseUrl()) }
    val isTestingConnection = remember { mutableStateOf(false) }
    val connectionMessage = remember { mutableStateOf<String?>(null) }
    val connectionOk = remember { mutableStateOf<Boolean?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val bgColor = Color(0xFFF7F4FF)
    val cardColor = Color.White
    val primaryColor = Color(0xFF5848E8)
    val softFieldColor = Color(0xFFF1F0F4)
    val textColor = Color(0xFF1F1F2E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(vertical = 16.dp)
    ) {
        // Header con botón atrás
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = primaryColor
                )
            }
            Text(
                text = "🔧 Configuración del Servidor",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(22.dp),
            color = cardColor,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "URL del Servidor",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Configura la URL base del servidor API. Asegúrate de incluir http:// o https://",
                    fontSize = 12.sp,
                    color = Color(0xFF999999),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = baseUrlState.value,
                    onValueChange = { baseUrlState.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("http://192.168.0.243:8080/api/") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = softFieldColor,
                        unfocusedContainerColor = softFieldColor,
                        disabledContainerColor = softFieldColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = primaryColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            ConfigManager.setBaseUrl(baseUrlState.value)
                            baseUrlState.value = ConfigManager.getBaseUrl()
                            RetrofitClient.invalidate()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("✅ Guardar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            ConfigManager.resetBaseUrl()
                            baseUrlState.value = ConfigManager.getBaseUrl()
                            RetrofitClient.invalidate()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE8E8E8),
                            contentColor = textColor
                        )
                    ) {
                        Text("🔄 Restaurar", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        ConfigManager.setBaseUrl(baseUrlState.value)
                        baseUrlState.value = ConfigManager.getBaseUrl()
                        RetrofitClient.invalidate()
                        isTestingConnection.value = true
                        connectionMessage.value = null
                        connectionOk.value = null

                        coroutineScope.launch {
                            val result = withContext(Dispatchers.IO) {
                                RetrofitClient.probeServer(ConfigManager.getBaseUrl())
                            }
                            isTestingConnection.value = false
                            if (result.reachable) {
                                connectionOk.value = true
                                connectionMessage.value = "Conexion exitosa (${result.detail}) en ${result.testedUrl}"
                            } else {
                                connectionOk.value = false
                                connectionMessage.value = "No se pudo conectar: ${result.detail}"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2F6FED),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        if (isTestingConnection.value) "Probando conexion..." else "Probar conexion"
                    )
                }

                if (connectionMessage.value != null) {
                    val statusColor = when (connectionOk.value) {
                        true -> Color(0xFF1B8E3E)
                        false -> Color(0xFFD93025)
                        else -> Color(0xFF666666)
                    }
                    Text(
                        text = connectionMessage.value ?: "",
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    color = Color(0xFFFAFAFA)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "URL Actual:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF666666)
                        )
                        Text(
                            ConfigManager.getBaseUrl(),
                            fontSize = 10.sp,
                            color = primaryColor,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE8E8E8),
                contentColor = textColor
            )
        ) {
            Text("⬅️ Volver al Login", fontWeight = FontWeight.Bold)
        }
    }
}

