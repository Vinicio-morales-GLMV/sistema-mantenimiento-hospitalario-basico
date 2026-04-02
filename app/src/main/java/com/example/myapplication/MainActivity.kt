package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.WelcomeScreen
import com.example.myapplication.ui.LoginScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.MenuGeneralScreen
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.usuarios
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.InputStream
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import com.example.myapplication.ui.GestionarUsuariosScreen
import com.example.myapplication.ui.MantenimientoScreen
import com.example.myapplication.ui.AgregarMantenimientoScreen
import com.example.myapplication.ui.MetricasScreen
import java.util.concurrent.TimeUnit
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HospiTecNavHost()
                }
            }
        }
    }
}

// La función MantenimientoScreen ahora está en el archivo separado MantenimientoScreen.kt

suspend fun subirArchivoASupabase(context: Context, uri: Uri): String? {
    // Este método sube el archivo a Supabase Storage y retorna la URL pública
    val contentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".xlsx", context.cacheDir)
    inputStream?.use { input ->
        tempFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    val client = OkHttpClient()
    val requestBody = tempFile.asRequestBody("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".toMediaTypeOrNull())
    val multipartBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", tempFile.name, requestBody)
        .build()
    val request = Request.Builder()
                        .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/mantenimientos/${tempFile.name}")
        .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .post(multipartBody)
        .build()
    val response = client.newCall(request).execute()
    return if (response.isSuccessful) {
        // Construye la URL pública del archivo
                    "https://hwjkyhuvelarpoxkvtzb.supabase.co/storage/v1/object/public/mantenimientos/${tempFile.name}"
    } else {
        null
    }
}

suspend fun guardarMantenimientoSupabase(equipoId: Int?, fecha: String, descripcion: String, archivoUrl: String) {
    if (equipoId == null) return
    val client = OkHttpClient()
    val json = """
        {
            \"equipo_id\": $equipoId,
            \"fecha_mantenimiento\": \"$fecha\",
            \"descripcion\": \"$descripcion\",
            \"archivo_url\": \"$archivoUrl\"
        }
    """.trimIndent()
    val requestBody = okhttp3.RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
    val request = Request.Builder()
                        .url("https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/mantenimientos")
        .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
        .addHeader("Content-Type", "application/json")
        .post(requestBody)
        .build()
    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            println("Error al guardar mantenimiento: ${response.body?.string()}")
        }
    }
}

@Composable
fun RegistrarUsuarioScreen(onBackToMenu: () -> Unit = {}) {
    com.example.myapplication.ui.RegisterScreen(onBackToMenu = onBackToMenu)
}

@Composable
fun GestionarUsuariosScreen(onBackToMenu: () -> Unit = {}) {
    com.example.myapplication.ui.GestionarUsuariosScreen(onBackToMenu = onBackToMenu)
}

@Composable
fun AgregarMantenimientoScreen(onBackToMenu: () -> Unit = {}) {
    com.example.myapplication.ui.AgregarMantenimientoScreen(onBackToMenu = onBackToMenu)
}

@Composable
fun MetricasScreen(onBackToMenu: () -> Unit = {}) {
    com.example.myapplication.ui.MetricasScreen(onBackToMenu = onBackToMenu)
}

@Composable
fun HospiTecNavHost() {
    val navController = rememberNavController()
    var nombreUsuario by remember { mutableStateOf("") }
    var rolUsuario by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") }
            )
        }
        composable("login") {
            LoginScreen(
                onLogin = { email, password, nombre, rol ->
                    if (nombre != null && rol != null) {
                        // Usuario autenticado (local o Supabase)
                        nombreUsuario = nombre
                        rolUsuario = rol
                        navController.navigate("menuGeneral") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        // Fallback: intentar obtener información desde Supabase
                        // Primero intentar con la lista local
                        var user = usuarios.find { it.correo == email && it.contrasena == password }
                        
                        if (user != null) {
                            // Usuario encontrado en lista local
                            nombreUsuario = user.nombre
                            rolUsuario = user.rol
                            navController.navigate("menuGeneral") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // Si no está en la lista local, consultar Supabase
                            // Esto se maneja en el LoginScreen, pero necesitamos actualizar el estado aquí
                            // El LoginScreen ya autenticó al usuario, solo necesitamos obtener la información
                            coroutineScope.launch {
                                obtenerInformacionUsuarioSupabase(email, password) { nombre, rol ->
                                    nombreUsuario = nombre
                                    rolUsuario = rol
                                    navController.navigate("menuGeneral") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("menuGeneral") {
            // Configuración de roles y permisos
            val isAdmin = rolUsuario == "admin" || rolUsuario == "Administrador" || rolUsuario == "Administrador e Ingeniero biomédico"
            val isJefeHospital = nombreUsuario == "Santiago Nuñez" || rolUsuario == "jefe_hospital"
            val isIngenieroAdmin = nombreUsuario in listOf("Gabriel Leon", "Kerly Verdezoto")
            val isDoctor = rolUsuario == "doctor"
            val isUsuario = rolUsuario == "usuario"
            val isTecnico = rolUsuario == "tecnico"
            
            // Acceso completo para administradores, jefe del hospital e ingenieros biomédicos
            val tieneAccesoCompleto = isAdmin || isJefeHospital || isIngenieroAdmin
            
            // Solo instrumentaria para doctores y usuarios
            val soloInstrumentaria = isDoctor || isUsuario
            
            MenuGeneralScreen(
                nombre = nombreUsuario,
                rol = rolUsuario,
                onInstrumentario = { navController.navigate("instrumentario") },
                onMantenimiento = when {
                    tieneAccesoCompleto -> ({ navController.navigate("mantenimiento") })
                    isTecnico -> ({ navController.navigate("mantenimiento") })
                    else -> null
                },
                onMetricas = when {
                    tieneAccesoCompleto -> ({ navController.navigate("metricas") })
                    isTecnico -> ({ navController.navigate("metricas") })
                    else -> null
                },
                onNotificaciones = when {
                    tieneAccesoCompleto -> ({ navController.navigate("notificaciones") })
                    isTecnico -> ({ navController.navigate("notificaciones") })
                    else -> null
                },
                onRegistrarUsuario = if (tieneAccesoCompleto) ({ navController.navigate("registrarUsuario") }) else null,
                onGestionarUsuarios = if (tieneAccesoCompleto) ({ navController.navigate("gestionarUsuarios") }) else null,
                onLogout = {
                    nombreUsuario = ""
                    rolUsuario = ""
                    navController.navigate("welcome") {
                        popUpTo("menuGeneral") { inclusive = true }
                    }
                }
            )
        }
        composable("instrumentario") {
            com.example.myapplication.ui.InstrumentarioScreen(
                onBackToMenu = { navController.navigate("menuGeneral") },
                nombreUsuario = nombreUsuario,
                rolUsuario = rolUsuario
            )
        }
        composable("mantenimiento") {
            MantenimientoScreen(
                onBackToMenu = { navController.navigate("menuGeneral") },
                nombreUsuario = nombreUsuario,
                rolUsuario = rolUsuario,
                onAgregarMantenimiento = { navController.navigate("agregarMantenimiento") }
            )
        }
        composable("agregarMantenimiento") {
            AgregarMantenimientoScreen(
                onBackToMenu = { navController.navigate("mantenimiento") },
                nombreUsuario = nombreUsuario,
                rolUsuario = rolUsuario
            )
        }
        composable("metricas") {
            MetricasScreen(
                onBackToMenu = { navController.navigate("menuGeneral") },
                nombreUsuario = nombreUsuario,
                rolUsuario = rolUsuario
            )
        }
        composable("notificaciones") {
            com.example.myapplication.ui.NotificacionesScreen(
                onBackToMenu = { navController.navigate("menuGeneral") },
                nombreUsuario = nombreUsuario,
                rolUsuario = rolUsuario
            )
        }
        composable("registrarUsuario") {
            RegistrarUsuarioScreen(onBackToMenu = { navController.navigate("menuGeneral") })
        }
        composable("gestionarUsuarios") {
            GestionarUsuariosScreen(onBackToMenu = { navController.navigate("menuGeneral") })
        }
    }
}

suspend fun obtenerInformacionUsuarioSupabase(email: String, password: String, onSuccess: (String, String) -> Unit) {
    try {
        println("🔍 MAIN: Obteniendo información del usuario desde Supabase")
        
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        // Primero buscar solo por email para verificar que el usuario existe
        val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios?email=eq.$email&select=nombre,apellido,email,password_hash,rol"
        
        val request = Request.Builder()
            .url(url)
            .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .get()
            .build()
        
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""
        
        println("🔍 MAIN: Código de respuesta: ${response.code}")
        println("🔍 MAIN: Respuesta: $responseBody")
        
        if (response.isSuccessful && responseBody.isNotEmpty()) {
            val jsonArray = JSONArray(responseBody)
            if (jsonArray.length() > 0) {
                val userObject = jsonArray.getJSONObject(0)
                val nombre = userObject.getString("nombre")
                val apellido = userObject.getString("apellido")
                val passwordHash = userObject.getString("password_hash")
                val rol = userObject.getString("rol")
                
                println("🔍 MAIN: Usuario encontrado en Supabase:")
                println("🔍 MAIN: - Nombre: $nombre")
                println("🔍 MAIN: - Apellido: $apellido")
                println("🔍 MAIN: - Password Hash: $passwordHash")
                println("🔍 MAIN: - Rol: $rol")
                println("🔍 MAIN: - Password ingresada: $password")
                
                // Verificar que la contraseña coincida
                if (passwordHash == password) {
                    println("🔍 MAIN: Contraseña válida")
                    val nombreCompleto = "$nombre $apellido"
                    println("🔍 MAIN: Usuario autenticado: $nombreCompleto - Rol: $rol")
                    onSuccess(nombreCompleto, rol)
                } else {
                    println("🔍 MAIN: Contraseña incorrecta")
                    println("🔍 MAIN: Hash almacenado: $passwordHash")
                    println("🔍 MAIN: Password ingresada: $password")
                }
            } else {
                println("🔍 MAIN: No se encontró usuario con ese email")
            }
        } else {
            println("🔍 MAIN: Error HTTP: ${response.code} - $responseBody")
        }
    } catch (e: Exception) {
        println("🔍 MAIN: Error obteniendo información del usuario: ${e.message}")
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        HospiTecNavHost()
    }
}