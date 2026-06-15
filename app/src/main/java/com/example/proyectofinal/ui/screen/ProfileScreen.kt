package com.example.proyectofinal.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinal.data.model.UsuarioUI
import com.example.proyectofinal.data.resources.t
import com.example.proyectofinal.ui.navigation.AppRoutes


@Composable
fun ProfileScreen(
    usuario: UsuarioUI,
    language: String,
    navController: NavController,
    isLoadingUsuario: Boolean,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onProfileImageChange: (String?) -> Unit,
    onRefreshUsuario: () -> Unit,
    onSave: () -> Unit,
    onDeleteUsuario: () -> Unit,
    onLogout: () -> Unit
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            onProfileImageChange(uri.toString())
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = t(language, "profile_title"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3A0CA3)
            )
        }

        // Imagen de perfil
        item {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF6C63FF), Color(0xFFFFB3C6))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {Image(
                painter = painterResource(android.R.drawable.ic_menu_camera),
                contentDescription = "Default icon",
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            }
        }

        item {
            OutlinedButton(
                onClick = { launcher.launch(arrayOf("image/*")) },
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(t(language, "change_photo"))
            }
        }

        // Datos del usuario
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Usuario remoto ID: ${usuario.id}", fontWeight = FontWeight.Bold)

                    if (isLoadingUsuario) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    OutlinedButton(
                        onClick = onRefreshUsuario,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoadingUsuario
                    ) {
                        Text("Actualizar")//Actualiza los datos del usuario desde la BD con la ayuda de la API
                    }

                    OutlinedTextField(
                        value = usuario.username,
                        onValueChange = onNombreChange,
                        label = { Text(t(language, "name")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = usuario.email,
                        onValueChange = onCorreoChange,
                        label = { Text(t(language, "email")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = usuario.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = usuario.description ?: "",
                        onValueChange = onBioChange,
                        label = { Text(t(language, "bio")) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoadingUsuario
                    ) {
                        Text(t(language, "save_changes"))
                    }

                    OutlinedButton(
                        onClick = onDeleteUsuario,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoadingUsuario
                    ) {
                        Text("Eliminar usuario")//Elimina el usuario de la BD con la ayuda de la API
                    }

                    OutlinedButton(
                        onClick = {
                            onLogout()
                            navController.navigate(AppRoutes.Login) {
                                popUpTo(AppRoutes.Home) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoadingUsuario
                    ) {
                        Text(t(language, "logout"))
                    }
                }
            }
        }
    }
}
