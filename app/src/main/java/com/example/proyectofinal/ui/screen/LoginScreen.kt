package com.example.proyectofinal.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.R
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.navigation.AppRoutes
import com.example.proyectofinal.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    language: String = "es",
    onLoginSuccess: (UsuarioUI) -> Unit,
    onRegisterClick: () -> Unit = {},
    navController: NavController? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var correo by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val bgColor = Color(0xFFF7F4FF)
    val cardColor = Color.White
    val primaryColor = Color(0xFF5848E8)
    val softFieldColor = Color(0xFFF1F0F4)
    val hintColor = Color(0xFF5F5F7A)
    val textColor = Color(0xFF1F1F2E)
    val accentGreen = Color(0xFF0BA896)
    val accentGold = Color(0xFFC08A00)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = t(language, "login_logo"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = cardColor,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = t(language, "login_title"),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = t(language, "login_subtitle"),
                        style = MaterialTheme.typography.bodyLarge,
                        color = hintColor
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Image(
                        painter = painterResource(id = R.drawable.login_icon),
                        contentDescription = "BattleIO logo",
                        modifier = Modifier.height(72.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(Color(0xFFD7D2E3))
                        )
                        Text(
                            text = "🛡",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = accentGold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(Color(0xFFD7D2E3))
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = t(language, "login_email"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F3F53)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = correo,
                            onValueChange = { correo = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("nombre@ejemplo.com") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = softFieldColor,
                                unfocusedContainerColor = softFieldColor,
                                disabledContainerColor = softFieldColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = primaryColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedPlaceholderColor = hintColor,
                                unfocusedPlaceholderColor = hintColor
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = t(language, "login_password"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F3F53)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("••••••••") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = softFieldColor,
                                unfocusedContainerColor = softFieldColor,
                                disabledContainerColor = softFieldColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = primaryColor,
                                focusedTextColor = textColor,
                                unfocusedTextColor = textColor,
                                focusedPlaceholderColor = hintColor,
                                unfocusedPlaceholderColor = hintColor
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.login(correo, password) { usuario ->
                                    if (usuario != null) {
                                        viewModel.cargarUsuarioDetalles(usuario.id)
                                        onLoginSuccess(usuario)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            enabled = !uiState.isLoading,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                contentColor = Color.White,
                                disabledContainerColor = primaryColor.copy(alpha = 0.65f)
                            )
                        ) {
                            Text(
                                text = t(language, "login_button"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (uiState.isLoading) {
                            Spacer(modifier = Modifier.height(14.dp))
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRegisterClick() },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = t(language, "login_register_prompt"),
                                color = Color(0xFF3F3F53)
                            )
                            Text(
                                text = " ${t(language, "login_register_action")}",
                                color = accentGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (navController != null) {
                IconButton(
                    onClick = { navController.navigate(AppRoutes.ServerConfig) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración del servidor",
                        tint = Color(0xFF5848E8),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
