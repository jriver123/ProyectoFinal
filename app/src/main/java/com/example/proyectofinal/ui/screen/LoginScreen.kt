package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.model.LoginResponse
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    language: String = "es",
    onLoginSuccess: (UsuarioUI) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var correo by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = t(language, "login_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
        }

        item {
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text(t(language, "email")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(t(language, "password")) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
        }

        item {
            Button(
                onClick = {
                    viewModel.login(correo, password) { usuario ->
                        if (usuario != null) {
                            viewModel.cargarUsuarioDetalles(usuario.id)
                            onLoginSuccess(usuario)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "login_button"))
            }
        }

        item {
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            uiState.message?.let {
                Text(text = it, color = Color(0xFF00A896))
            }
            uiState.errorMessage?.let {
                Text(text = it, color = Color(0xFFE63946))
            }
        }
    }
}
