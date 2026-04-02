package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.BorderStroke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.example.myapplication.ui.usuarios
import com.example.myapplication.ui.Usuario
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(
    onLogin: (String, String, String?, String?) -> Unit,
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            color = HospitalBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                errorMessage = "" // Limpiar error cuando el usuario escribe
            },
            label = { Text("Correo electrónico", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                errorMessage = "" // Limpiar error cuando el usuario escribe
            },
            label = { Text("Contraseña", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.White
                    )
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                println("LOGIN_DEBUG: Intentando iniciar sesión")
                println("LOGIN_DEBUG: Email: $email")
                println("LOGIN_DEBUG: Password: ${password.take(3)}***")
                
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Por favor completa todos los campos"
                    return@Button
                }
                
                isLoading = true
                errorMessage = ""
                
                // Simular un pequeño delay para mostrar el loading
                CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    
                    try {
                        println("LOGIN_DEBUG: Buscando usuario...")
                        
                        // Primero intentar con la lista local (usuarios existentes)
                        var user = usuarios.find { it.correo == email && it.contrasena == password }
                        
                        if (user != null) {
                            println("LOGIN_DEBUG: Usuario encontrado en lista local: ${user.nombre}")
                            println("LOGIN_DEBUG: Rol: ${user.rol}")
                            onLogin(email, password, user.nombre, user.rol)
                        } else {
                            // Si no está en la lista local, consultar Supabase
                            println("LOGIN_DEBUG: Usuario no encontrado en lista local, consultando Supabase...")
                            user = autenticarUsuarioSupabase(email, password)
                            
                            if (user != null) {
                                println("LOGIN_DEBUG: Usuario autenticado en Supabase: ${user.nombre}")
                                println("LOGIN_DEBUG: Rol: ${user.rol}")
                                onLogin(email, password, user.nombre, user.rol)
                            } else {
                                println("LOGIN_DEBUG: Usuario no encontrado en Supabase")
                                errorMessage = "Credenciales incorrectas"
                            }
                        }
                    } catch (e: Exception) {
                        println("LOGIN_DEBUG: Error en login: ${e.message}")
                        errorMessage = "Error al iniciar sesión: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, HospitalBlue)
        ) {
            Text(
                text = "Volver",
                color = HospitalBlue,
                fontSize = 16.sp
            )
        }
    }
}

suspend fun autenticarUsuarioSupabase(email: String, password: String): Usuario? {
    return withContext(Dispatchers.IO) {
        try {
            println("🔍 AUTENTICACION: Consultando Supabase para email: $email")
            
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            // Primero buscar solo por email para verificar que el usuario existe
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios?email=eq.$email&select=nombre,apellido,email,password_hash,rol"
            println("🔍 AUTENTICACION: URL: $url")
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .get()
                .build()
            
            println("🔍 AUTENTICACION: Ejecutando petición GET...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string() ?: ""
            
            println("🔍 AUTENTICACION: Código de respuesta: $responseCode")
            println("🔍 AUTENTICACION: Respuesta: $responseBody")
            
            if (response.isSuccessful && responseBody.isNotEmpty()) {
                try {
                    val jsonArray = JSONArray(responseBody)
                    if (jsonArray.length() > 0) {
                        val userObject = jsonArray.getJSONObject(0)
                        val nombre = userObject.getString("nombre")
                        val apellido = userObject.getString("apellido")
                        val email = userObject.getString("email")
                        val passwordHash = userObject.getString("password_hash")
                        val rol = userObject.getString("rol")
                        
                        println("🔍 AUTENTICACION: Usuario encontrado en Supabase:")
                        println("🔍 AUTENTICACION: - Nombre: $nombre")
                        println("🔍 AUTENTICACION: - Apellido: $apellido")
                        println("🔍 AUTENTICACION: - Email: $email")
                        println("🔍 AUTENTICACION: - Password Hash: $passwordHash")
                        println("🔍 AUTENTICACION: - Rol: $rol")
                        println("🔍 AUTENTICACION: - Password ingresada: $password")
                        
                        // Verificar que la contraseña coincida
                        if (passwordHash == password) {
                            println("🔍 AUTENTICACION: Contraseña válida")
                            return@withContext Usuario(
                                nombre = "$nombre $apellido",
                                correo = email,
                                contrasena = password,
                                rol = rol
                            )
                        } else {
                            println("🔍 AUTENTICACION: Contraseña incorrecta")
                            println("🔍 AUTENTICACION: Hash almacenado: $passwordHash")
                            println("🔍 AUTENTICACION: Password ingresada: $password")
                        }
                    } else {
                        println("🔍 AUTENTICACION: No se encontró usuario con ese email")
                    }
                } catch (e: Exception) {
                    println("🔍 AUTENTICACION: Error procesando JSON: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("🔍 AUTENTICACION: Error HTTP: $responseCode - $responseBody")
            }
            
            return@withContext null
        } catch (e: Exception) {
            println("🔍 AUTENTICACION: Excepción: ${e.message}")
            e.printStackTrace()
            return@withContext null
        }
    }
} 