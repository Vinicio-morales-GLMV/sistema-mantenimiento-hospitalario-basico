package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun GestionarUsuariosScreen(onBackToMenu: () -> Unit = {}) {
    val scope = rememberCoroutineScope()
    var usuarios by remember { mutableStateOf(listOf<UsuarioData>()) }
    var usuarioEditando by remember { mutableStateOf<UsuarioData?>(null) }
    var mensaje by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) } // NUEVO: estado de carga
    var recargar by remember { mutableStateOf(false) } // NUEVO: para forzar recarga

    // Cargar usuarios al iniciar o al recargar
    LaunchedEffect(recargar) {
        loading = true
        error = ""
        try {
            usuarios = obtenerUsuariosSupabase()
            loading = false
        } catch (e: Exception) {
            val mensajeError = e.message
            error = "Error al cargar usuarios: " + (mensajeError ?: "desconocido")
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(16.dp)
            .padding(top = 32.dp), // Agregar más espacio arriba para evitar la cámara
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Usuarios registrados", color = HospitalBlue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onBackToMenu, 
                shape = RoundedCornerShape(12.dp), 
                colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
                modifier = Modifier.padding(4.dp)
            ) {
                Text("Volver", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
        Spacer(Modifier.height(16.dp))
        if (mensaje.isNotEmpty()) Text(mensaje, color = Color.Green)
        if (error.isNotEmpty()) {
            Text(error, color = Color.Red)
            Button(onClick = { recargar = !recargar }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Reintentar")
            }
        }
        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = HospitalBlue)
            }
        } else if (usuarioEditando == null) {
            if (usuarios.isEmpty() && error.isEmpty()) {
                Text("No hay usuarios registrados.", color = Color.Gray, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(usuarios) { usuario ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(usuario.correo, color = Color.Gray)
                                    Text(usuario.rol, color = HospitalBlue)
                                }
                                IconButton(onClick = { usuarioEditando = usuario }) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = HospitalBlue)
                                }
                                IconButton(onClick = {
                                    scope.launch {
                                        val exito = eliminarUsuarioSupabase(usuario.id)
                                        if (exito) {
                                            mensaje = "Usuario eliminado"
                                            recargar = !recargar // Fuerza recarga
                                        } else {
                                            error = "Error al eliminar usuario"
                                        }
                                    }
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            EditarUsuarioForm(
                usuario = usuarioEditando!!,
                onGuardar = { actualizado ->
                    scope.launch {
                        val exito = actualizarUsuarioSupabase(actualizado)
                        if (exito) {
                            mensaje = "Usuario actualizado"
                            usuarioEditando = null
                            recargar = !recargar // Fuerza recarga
                        } else {
                            error = "Error al actualizar usuario"
                        }
                    }
                },
                onCancelar = { usuarioEditando = null }
            )
        }
    }
}

data class UsuarioData(
    val id: Int,
    var nombre: String,
    var correo: String,
    var rol: String
)

suspend fun obtenerUsuariosSupabase(): List<UsuarioData> {
    return withContext(Dispatchers.IO) {
        try {
            println("Iniciando conexión a Supabase...")
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios")
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .build()
            
            println("Ejecutando petición HTTP...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val body = response.body?.string() ?: ""
            
            // Log para debug
            println("Supabase Response Code: $responseCode")
            println("Supabase Response Body: $body")
            
            if (!response.isSuccessful) {
                println("Error en la respuesta: $responseCode - $body")
                throw Exception("Error HTTP: $responseCode - $body")
            }
            
            if (body.isEmpty()) {
                println("Respuesta vacía de Supabase")
                return@withContext emptyList()
            }
            
            val jsonArray = JSONArray(body)
            println("Usuarios encontrados: ${jsonArray.length()}")
            
            List(jsonArray.length()) { i ->
                val obj = jsonArray.getJSONObject(i)
                UsuarioData(
                    id = obj.getInt("id"),
                    nombre = obj.getString("nombre"),
                    correo = obj.getString("email"),
                    rol = obj.getString("rol")
                )
            }
        } catch (e: Exception) {
            println("Error al obtener usuarios: ${e.message}")
            println("Tipo de excepción: ${e.javaClass.simpleName}")
            println("Stack trace completo:")
            e.printStackTrace()
            throw e
        } catch (e: Throwable) {
            println("Error general al obtener usuarios: ${e.message}")
            println("Tipo de excepción: ${e.javaClass.simpleName}")
            e.printStackTrace()
            throw Exception("Error de red o conexión: ${e.message}")
        }
    }
}

suspend fun eliminarUsuarioSupabase(id: Int): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            println("Eliminando usuario con ID: $id")
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios?id=eq.$id")
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .delete()
                .build()
            
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val body = response.body?.string() ?: ""
            
            println("Respuesta de eliminación: $responseCode - $body")
            
            if (!response.isSuccessful) {
                println("Error al eliminar usuario: $responseCode - $body")
                return@withContext false
            }
            
            return@withContext true
        } catch (e: Exception) {
            println("Error al eliminar usuario: ${e.message}")
            e.printStackTrace()
            return@withContext false
        }
    }
}

suspend fun actualizarUsuarioSupabase(usuario: UsuarioData): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            println("Actualizando usuario con ID: ${usuario.id}")
            val client = OkHttpClient()
            val json = """
                {
                    "nombre": "${usuario.nombre}",
                    "email": "${usuario.correo}",
                    "rol": "${usuario.rol}"
                }
            """.trimIndent()
            
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios?id=eq.${usuario.id}")
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .patch(requestBody)
                .build()
            
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val body = response.body?.string() ?: ""
            
            println("Respuesta de actualización: $responseCode - $body")
            
            if (!response.isSuccessful) {
                println("Error al actualizar usuario: $responseCode - $body")
                return@withContext false
            }
            
            return@withContext true
        } catch (e: Exception) {
            println("Error al actualizar usuario: ${e.message}")
            e.printStackTrace()
            return@withContext false
        }
    }
}

@Composable
fun EditarUsuarioForm(usuario: UsuarioData, onGuardar: (UsuarioData) -> Unit, onCancelar: () -> Unit) {
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var correo by remember { mutableStateOf(usuario.correo) }
    var rol by remember { mutableStateOf(usuario.rol) }
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)
    ) {
        Text("Editar usuario", color = HospitalBlue, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
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
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { onGuardar(usuario.copy(nombre = nombre, correo = correo, rol = rol)) }, colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue)) {
                Text("Guardar", color = Color.White)
            }
            Button(onClick = onCancelar, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("Cancelar", color = HospitalBlue)
            }
        }
    }
} 