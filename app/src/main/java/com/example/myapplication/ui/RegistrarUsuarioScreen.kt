package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun RegistrarUsuarioScreen(onBackToMenu: () -> Unit = {}) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("jefe_hospital") }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrar nuevo usuario",
            color = HospitalBlue,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = HospitalBlue) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = HospitalBlue) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
        )
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = HospitalBlue) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp)
        )
        // Selector de rol
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = HospitalBlue)
            ) {
                Text(rol, fontSize = 20.sp)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                rolesDisponibles.forEach { r ->
                    DropdownMenuItem(
                        text = { Text(r) },
                        onClick = {
                            rol = r
                            expanded = false
                        }
                    )
                }
            }
        }
        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = Color.Green, modifier = Modifier.padding(bottom = 8.dp))
        }
        if (error.isNotEmpty()) {
            Text(error, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }
        Button(
            onClick = {
                error = ""
                mensaje = ""
                if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                    error = "Todos los campos son obligatorios."
                    return@Button
                }
                if (!correo.contains("@") || !correo.contains(".")) {
                    error = "Correo inválido."
                    return@Button
                }
                scope.launch {
                    val exito = registrarUsuarioSupabase(nombre, correo, contrasena, rol)
                    if (exito) {
                        mensaje = "Usuario registrado exitosamente."
                        nombre = ""
                        correo = ""
                        contrasena = ""
                        rol = "jefe_hospital"
                    } else {
                        error = "Error al registrar usuario."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Registrar", fontSize = 23.sp, color = Color.White)
        }
        Button(
            onClick = onBackToMenu,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(60.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Volver al menú principal", fontSize = 18.sp, color = HospitalBlue)
        }
    }
}

suspend fun registrarUsuarioSupabase(nombre: String, correo: String, contrasena: String, rol: String): Boolean {
    val client = OkHttpClient()
    val json = """
        {\n  \"nombre\": \"$nombre\",\n  \"email\": \"$correo\",\n  \"password_hash\": \"$contrasena\",\n  \"rol\": \"$rol\"\n}"""
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    val request = Request.Builder()
                        .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios")
        .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()
    val response = client.newCall(request).execute()
    return response.isSuccessful
} 