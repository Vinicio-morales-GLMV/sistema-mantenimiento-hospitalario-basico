package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBackToMenu: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRol by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(16.dp)
            .padding(top = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Registrar Nuevo Usuario",
            color = HospitalBlue,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(24.dp))
        
        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = Color.Green, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
        }
        
        if (error.isNotEmpty()) {
            Text(error, color = Color.Red, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
        }

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black
            )
        )
        
        Spacer(Modifier.height(16.dp))

        // Campo Apellido
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black
            )
        )
        
        Spacer(Modifier.height(16.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico", color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black
            )
        )
        
        Spacer(Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.White) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black
            )
        )
        
        Spacer(Modifier.height(16.dp))

        // Campo Confirmar Contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña", color = Color.White) },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black
            )
        )
        
        Spacer(Modifier.height(16.dp))

        // Dropdown para Rol
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HospitalBlue)
            ) {
                val rolDisplay = when (selectedRol) {
                    "admin" -> "Administrador"
                    "tecnico" -> "Técnico en mantenimiento"
                    "doctor" -> "Doctor"
                    "usuario" -> "Usuario"
                    else -> "Seleccionar rol"
                }
                Text(rolDisplay, fontSize = 16.sp)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                listOf(
                    "admin" to "Administrador",
                    "tecnico" to "Técnico en mantenimiento", 
                    "doctor" to "Doctor",
                    "usuario" to "Usuario"
                ).forEach { (rolValue, rolDisplay) ->
                    DropdownMenuItem(
                        text = { Text(rolDisplay) },
                        onClick = {
                            selectedRol = rolValue // Guardar el valor interno del rol
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))

        // Botón Registrar
        Button(
            onClick = {
                println("🔍 REGISTRO: Botón presionado")
                scope.launch {
                    try {
                        println("🔍 REGISTRO: Iniciando validación")
                        if (validarDatos(nombre, apellido, email, password, confirmPassword, selectedRol)) {
                            println("🔍 REGISTRO: Validación exitosa")
                            isLoading = true
                            error = ""
                            mensaje = ""
                            
                            println("🔍 REGISTRO: Datos a enviar:")
                            println("🔍 REGISTRO: - Nombre: $nombre")
                            println("🔍 REGISTRO: - Apellido: $apellido")
                            println("🔍 REGISTRO: - Email: $email")
                            println("🔍 REGISTRO: - Rol: $selectedRol")
                            
                            val exito = crearUsuarioSupabase(nombre, apellido, email, password, selectedRol)
                            
                            if (exito) {
                                println("🔍 REGISTRO: Registro exitoso")
                                mensaje = "✅ Usuario registrado exitosamente"
                                // Limpiar campos
                                nombre = ""
                                apellido = ""
                                email = ""
                                password = ""
                                confirmPassword = ""
                                selectedRol = ""
                                println("🔍 REGISTRO: Campos limpiados")
                            } else {
                                println("🔍 REGISTRO: Error en el registro")
                                error = "❌ Error al registrar usuario. Verifica la conexión y los datos."
                            }
                            isLoading = false
                        } else {
                            println("🔍 REGISTRO: Validación fallida")
                            error = "❌ Por favor completa todos los campos correctamente"
                        }
                    } catch (e: Exception) {
                        println("🔍 REGISTRO: Exception: ${e.message}")
                        e.printStackTrace()
                        error = "❌ Error inesperado: ${e.message}"
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text("Registrar Usuario", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
        
        Spacer(Modifier.height(16.dp))

        // Botón Volver
        Button(
            onClick = onBackToMenu,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Volver", color = HospitalBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

fun validarDatos(nombre: String, apellido: String, email: String, password: String, confirmPassword: String, rol: String): Boolean {
    if (nombre.isBlank() || apellido.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || rol.isBlank()) {
        return false
    }
    
    if (!email.contains("@")) {
        return false
    }
    
    if (password != confirmPassword) {
        return false
    }
    
    if (password.length < 6) {
        return false
    }
    
    return true
}

suspend fun crearUsuarioSupabase(nombre: String, apellido: String, email: String, password: String, rol: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            println("🔍 REGISTRO: Intentando crear usuario: $email")
            
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
                
            val json = """
                {
                    "nombre": "$nombre",
                    "apellido": "$apellido",
                    "email": "$email",
                    "password_hash": "$password",
                    "rol": "$rol"
                }
            """.trimIndent()
            
            println("🔍 REGISTRO: JSON payload: $json")
            
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios")
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            println("🔍 REGISTRO: Response code: ${response.code}")
            println("🔍 REGISTRO: Response body: $responseBody")
            
            if (response.isSuccessful) {
                println("🔍 REGISTRO: Usuario creado exitosamente")
                return@withContext true
            } else {
                println("🔍 REGISTRO: Error HTTP: ${response.code} - ${response.message}")
                println("🔍 REGISTRO: Error body: $responseBody")
                return@withContext false
            }
        } catch (e: Exception) {
            println("🔍 REGISTRO: Exception: ${e.message}")
            e.printStackTrace()
            return@withContext false
        }
    }
} 