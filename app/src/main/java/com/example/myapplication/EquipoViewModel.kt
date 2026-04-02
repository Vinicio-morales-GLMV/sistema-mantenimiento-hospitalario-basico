package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data class para notificaciones reales
data class NotificacionReal(
    val id: Int,
    val equipoId: Int,
    val equipoNombre: String,
    val equipoCodigo: String,
    val ubicacion: String,
    val reportadoPor: String,
    val fecha: String,
    val descripcion: String?,
    val leida: Boolean = false
)

class EquipoViewModel : ViewModel() {
    
    private val _equipos = MutableStateFlow<List<Equipo>>(emptyList())
    val equipos: StateFlow<List<Equipo>> = _equipos.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Lista de notificaciones reales - Compartida entre instancias
    companion object {
        private val _notificacionesCompartidas = MutableStateFlow<List<NotificacionReal>>(emptyList())
    }
    
    private val _notificaciones = _notificacionesCompartidas
    val notificaciones: StateFlow<List<NotificacionReal>> = _notificaciones.asStateFlow()

    init {
        println("🔧 INIT: Iniciando EquipoViewModel")
        println("🔧 INIT: Notificaciones actuales: ${_notificacionesCompartidas.value.size}")
        cargarEquipos()
    }

    fun cargarEquipos() {
        println("🔄 CARGAR: Iniciando carga de equipos")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val equiposObtenidos = obtenerEquiposDirecto()
                println("🔄 CARGAR: Equipos obtenidos: ${equiposObtenidos.size}")
                
                _equipos.value = equiposObtenidos
                _isLoading.value = false
                
                if (equiposObtenidos.isEmpty()) {
                    println("🔄 CARGAR: ⚠️ No se obtuvieron equipos")
                    _error.value = "No se encontraron equipos en la base de datos."
                } else {
                    println("🔄 CARGAR: ✅ Carga exitosa con ${equiposObtenidos.size} equipos")
                    _error.value = null
                }
            } catch (e: Exception) {
                println("🔄 CARGAR: ❌ Error al cargar equipos: ${e.message}")
                e.printStackTrace()
                _error.value = "Error al cargar equipos: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun actualizarEstadoEquipo(id: Int, nuevoEstado: String, descripcionProblema: String? = null, nombreUsuario: String = "") {
        viewModelScope.launch {
            try {
                println("🔄 ACTUALIZAR: Actualizando estado del equipo $id a '$nuevoEstado'")
                println("🔄 ACTUALIZAR: Descripción problema: $descripcionProblema")
                println("🔄 ACTUALIZAR: Usuario: $nombreUsuario")
                _isLoading.value = true
                
                val success = actualizarEstadoEquipoDirecto(id, nuevoEstado)
                
                if (success) {
                    println("🔄 ACTUALIZAR: ✅ Estado actualizado exitosamente en Supabase")
                    
                    // Si es "sin funcionar" y hay descripción, crear mantenimiento automático
                    if (nuevoEstado == "Sin funcionar" && !descripcionProblema.isNullOrBlank()) {
                        println("🔄 ACTUALIZAR: 🚨 Creando mantenimiento automático para equipo $id")
                        val mantenimientoCreado = crearMantenimientoAutomatico(id, descripcionProblema, nombreUsuario)
                        if (mantenimientoCreado) {
                            println("🔄 ACTUALIZAR: ✅ Mantenimiento automático creado exitosamente")
                        } else {
                            println("🔄 ACTUALIZAR: ⚠️ Error al crear mantenimiento automático")
                        }
                    }
                    
                    // Enviar notificación si un doctor cambió el estado a "sin funcionar"
                    if (nuevoEstado == "Sin funcionar" && nombreUsuario.isNotBlank()) {
                        println("🔄 ACTUALIZAR: 🔔 Enviando notificación por cambio de estado a 'sin funcionar'")
                        enviarNotificacionEquipoSinFuncionar(id, nombreUsuario, descripcionProblema)
                    }
                    
                    cargarEquipos() // Recargar datos desde Supabase
                } else {
                    println("🔄 ACTUALIZAR: ❌ Error al actualizar estado en Supabase")
                    _error.value = "Error al actualizar el estado del equipo en la base de datos"
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                println("🔄 ACTUALIZAR: ❌ Excepción: ${e.message}")
                e.printStackTrace()
                _error.value = "Error al actualizar estado: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun crearMantenimientoAutomatico(equipoId: Int, descripcion: String, nombreUsuario: String): Boolean = withContext(Dispatchers.IO) {
        try {
            println("🔧 MANTENIMIENTO: Creando mantenimiento automático")
            println("🔧 MANTENIMIENTO: Equipo ID: $equipoId")
            println("🔧 MANTENIMIENTO: Descripción: $descripcion")
            println("🔧 MANTENIMIENTO: Usuario: $nombreUsuario")
            
            val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val descripcionCompleta = "Mantenimiento automático generado por doctor: $descripcion"
            
            val client = OkHttpClient()
            val json = """
                {
                    "equipo_id": $equipoId,
                    "fecha_mantenimiento": "$fechaActual",
                    "descripcion": "$descripcionCompleta",
                    "tipo": "Correctivo",
                    "tecnico": "$nombreUsuario",
                    "costo": 0,
                    "observaciones": "Mantenimiento automático generado al cambiar estado a 'Sin funcionar'",
                    "archivo_url": null
                }
            """.trimIndent()
            
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/mantenimientos"
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()
            
            println("🔧 MANTENIMIENTO: Ejecutando POST...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string() ?: ""
            
            println("🔧 MANTENIMIENTO: Código: $responseCode")
            println("🔧 MANTENIMIENTO: Respuesta: $responseBody")
            println("🔧 MANTENIMIENTO: ¿Éxito?: ${response.isSuccessful}")
            
            response.isSuccessful
        } catch (e: Exception) {
            println("🔧 MANTENIMIENTO: ❌ Excepción: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    fun crearNuevoEquipo(
        codigo: String,
        nombre: String,
        tipo: String,
        marca: String,
        modelo: String,
        serie: String,
        ubicacion: String,
        estado: String = "Funcionando",
        fechaAdquisicion: String = "",
        proximoMantenimiento: String = "",
        nombreUsuario: String = ""
    ) {
        viewModelScope.launch {
            try {
                println("🔧 CREAR: Creando nuevo equipo")
                println("🔧 CREAR: Código: $codigo")
                println("🔧 CREAR: Nombre: $nombre")
                println("🔧 CREAR: Usuario: $nombreUsuario")
                
                _isLoading.value = true
                
                val success = crearEquipoDirecto(
                    codigo, nombre, tipo, marca, modelo, serie, ubicacion,
                    estado, fechaAdquisicion, proximoMantenimiento, nombreUsuario
                )
                
                if (success) {
                    println("🔧 CREAR: ✅ Equipo creado exitosamente")
                    cargarEquipos() // Recargar datos desde Supabase
                } else {
                    println("🔧 CREAR: ❌ Error al crear equipo")
                    _error.value = "Error al crear el equipo en la base de datos"
                }
                
                _isLoading.value = false
            } catch (e: Exception) {
                println("🔧 CREAR: ❌ Excepción: ${e.message}")
                e.printStackTrace()
                _error.value = "Error al crear equipo: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun crearEquipoDirecto(
        codigo: String,
        nombre: String,
        tipo: String,
        marca: String,
        modelo: String,
        serie: String,
        ubicacion: String,
        estado: String,
        fechaAdquisicion: String,
        proximoMantenimiento: String,
        nombreUsuario: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            println("🔧 CREAR: Iniciando creación en Supabase")
            
            val client = OkHttpClient()
            val json = """
                {
                    "codigo": "$codigo",
                    "nombre": "$nombre",
                    "tipo": "$tipo",
                    "marca": "$marca",
                    "modelo": "$modelo",
                    "serie": "$serie",
                    "ubicacion": "$ubicacion",
                    "estado": "$estado",
                    "fecha_adquisicion": "$fechaAdquisicion",
                    "proximo_mantenimiento": "$proximoMantenimiento",
                    "last_modified_by": "$nombreUsuario"
                }
            """.trimIndent()
            
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/instrumentos"
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()
            
            println("🔧 CREAR: Ejecutando POST...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string() ?: ""
            
            println("🔧 CREAR: Código: $responseCode")
            println("🔧 CREAR: Respuesta: $responseBody")
            println("🔧 CREAR: ¿Éxito?: ${response.isSuccessful}")
            
            response.isSuccessful
        } catch (e: Exception) {
            println("🔧 CREAR: ❌ Excepción: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private suspend fun obtenerEquiposDirecto(): List<Equipo> = withContext(Dispatchers.IO) {
        try {
            println("🔍 DIAGNÓSTICO: Iniciando obtención de equipos desde Supabase")
            
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/instrumentos?select=id,codigo,nombre,estado,tipo,marca,modelo,serie,ubicacion,fecha_adquisicion,proximo_mantenimiento,fecha_creacion,last_modified_by"
            println("🔍 DIAGNÓSTICO: URL: $url")
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .get()
                .build()
            
            println("🔍 DIAGNÓSTICO: Ejecutando petición GET...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            val responseBody = response.body?.string() ?: ""
            
            println("🔍 DIAGNÓSTICO: Código de respuesta: $responseCode")
            println("🔍 DIAGNÓSTICO: Tamaño de respuesta: ${responseBody.length} caracteres")
            println("🔍 DIAGNÓSTICO: Respuesta completa: $responseBody")
            println("🔍 DIAGNÓSTICO: Headers de respuesta:")
            response.headers.forEach { header ->
                println("🔍 DIAGNÓSTICO: ${header.first}: ${header.second}")
            }
            
            if (response.isSuccessful) {
                println("🔍 DIAGNÓSTICO: ✅ Respuesta exitosa")
                if (responseBody.isNotEmpty()) {
                    try {
                        val jsonArray = JSONArray(responseBody)
                        println("🔍 DIAGNÓSTICO: ✅ JSON Array creado, tamaño: ${jsonArray.length()}")
                        
                        val equipos = mutableListOf<Equipo>()
                        
                        for (i in 0 until jsonArray.length()) {
                            try {
                                val jsonObject = jsonArray.getJSONObject(i)
                                println("🔍 DIAGNÓSTICO: Procesando objeto $i")
                                
                                val id = jsonObject.optInt("id", 0)
                                val codigo = jsonObject.optString("codigo", "")
                                val nombre = jsonObject.optString("nombre", "")
                                val estado = jsonObject.optString("estado", "")
                                val ubicacion = jsonObject.optString("ubicacion", "")
                                
                                println("🔍 DIAGNÓSTICO: Equipo $id - Código: '$codigo' - Nombre: '$nombre' - Estado: '$estado' - Ubicación: '$ubicacion'")
                                
                                val equipo = Equipo(
                                    id = id,
                                    codigo = codigo,
                                    tipo = jsonObject.optString("tipo", ""),
                                    nombre = nombre,
                                    marca = jsonObject.optString("marca", ""),
                                    modelo = jsonObject.optString("modelo", ""),
                                    serie = jsonObject.optString("serie", ""),
                                    ubicacion = ubicacion,
                                    estado = estado,
                                    fecha_adquisicion = jsonObject.optString("fecha_adquisicion", ""),
                                    proximo_mantenimiento = jsonObject.optString("proximo_mantenimiento", ""),
                                    responsable = null, // Esta columna no existe en Supabase
                                    observaciones = null, // Esta columna no existe en Supabase
                                    fecha_creacion = jsonObject.optString("fecha_creacion", ""),
                                    last_modified_by = jsonObject.optString("last_modified_by", "")
                                )
                                equipos.add(equipo)
                                println("🔍 DIAGNÓSTICO: ✅ Equipo agregado: ${equipo.nombre} - Estado: ${equipo.estado}")
                            } catch (e: Exception) {
                                println("🔍 DIAGNÓSTICO: ❌ Error procesando equipo $i: ${e.message}")
                                e.printStackTrace()
                            }
                        }
                        
                        println("🔍 DIAGNÓSTICO: ✅ Total de equipos procesados: ${equipos.size}")
                        equipos
                    } catch (e: Exception) {
                        println("🔍 DIAGNÓSTICO: ❌ Error procesando JSON: ${e.message}")
                        e.printStackTrace()
                        emptyList()
                    }
                } else {
                    println("🔍 DIAGNÓSTICO: ❌ Respuesta vacía")
                    emptyList()
                }
            } else {
                println("🔍 DIAGNÓSTICO: ❌ Error al obtener equipos: $responseCode - $responseBody")
                emptyList()
            }
        } catch (e: Exception) {
            println("🔍 DIAGNÓSTICO: ❌ Excepción al obtener equipos: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    private suspend fun actualizarEstadoEquipoDirecto(id: Int, nuevoEstado: String): Boolean = withContext(Dispatchers.IO) {
        try {
            println("🔧 ACTUALIZAR: Iniciando actualización en Supabase")
            println("🔧 ACTUALIZAR: ID: $id, Estado: '$nuevoEstado'")
            
            val client = OkHttpClient()
            val json = """
                {
                    "estado": "$nuevoEstado"
                }
            """.trimIndent()
            
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/instrumentos?id=eq.$id"
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .patch(requestBody)
                .build()
            
            println("🔧 ACTUALIZAR: Ejecutando PATCH...")
            val response = client.newCall(request).execute()
            val responseCode = response.code
            
            println("🔧 ACTUALIZAR: Código: $responseCode")
            println("🔧 ACTUALIZAR: ¿Éxito?: ${response.isSuccessful}")
            
            response.isSuccessful
        } catch (e: Exception) {
            println("🔧 ACTUALIZAR: ❌ Excepción: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private suspend fun enviarNotificacionEquipoSinFuncionar(equipoId: Int, nombreUsuario: String, descripcionProblema: String?) {
        try {
            println("🔔 NOTIFICACIÓN: Enviando notificación por equipo sin funcionar")
            println("🔔 NOTIFICACIÓN: Equipo ID: $equipoId")
            println("🔔 NOTIFICACIÓN: Usuario: $nombreUsuario")
            println("🔔 NOTIFICACIÓN: Descripción: $descripcionProblema")
            
            // Obtener información del equipo
            val equipos = _equipos.value
            val equipo = equipos.find { it.id == equipoId }
            
            if (equipo != null) {
                val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                
                // Crear notificación real
                val nuevaNotificacion = NotificacionReal(
                    id = System.currentTimeMillis().toInt(), // ID único basado en timestamp
                    equipoId = equipoId,
                    equipoNombre = equipo.nombre ?: "Equipo desconocido",
                    equipoCodigo = equipo.codigo ?: "N/A",
                    ubicacion = equipo.ubicacion ?: "Ubicación desconocida",
                    reportadoPor = nombreUsuario,
                    fecha = fechaActual,
                    descripcion = descripcionProblema,
                    leida = false
                )
                
                // Agregar la notificación a la lista compartida
                val notificacionesActuales = _notificacionesCompartidas.value.toMutableList()
                notificacionesActuales.add(0, nuevaNotificacion) // Agregar al inicio
                _notificacionesCompartidas.value = notificacionesActuales
                
                println("🔔 NOTIFICACIÓN: ✅ Notificación real creada y agregada")
                println("🔔 NOTIFICACIÓN: - Equipo: ${nuevaNotificacion.equipoNombre}")
                println("🔔 NOTIFICACIÓN: - Código: ${nuevaNotificacion.equipoCodigo}")
                println("🔔 NOTIFICACIÓN: - Ubicación: ${nuevaNotificacion.ubicacion}")
                println("🔔 NOTIFICACIÓN: - Reportado por: ${nuevaNotificacion.reportadoPor}")
                println("🔔 NOTIFICACIÓN: - Fecha: ${nuevaNotificacion.fecha}")
                println("🔔 NOTIFICACIÓN: - Descripción: ${nuevaNotificacion.descripcion}")
                println("🔔 NOTIFICACIÓN: Total de notificaciones: ${_notificacionesCompartidas.value.size}")
                
            } else {
                println("🔔 NOTIFICACIÓN: ⚠️ No se encontró información del equipo $equipoId")
            }
            
        } catch (e: Exception) {
            println("🔔 NOTIFICACIÓN: ❌ Error enviando notificación: ${e.message}")
            e.printStackTrace()
        }
    }
    
    // Función para marcar notificación como leída
    fun marcarNotificacionComoLeida(notificacionId: Int) {
        val notificacionesActuales = _notificacionesCompartidas.value.toMutableList()
        val index = notificacionesActuales.indexOfFirst { it.id == notificacionId }
        if (index != -1) {
            notificacionesActuales[index] = notificacionesActuales[index].copy(leida = true)
            _notificacionesCompartidas.value = notificacionesActuales
            println("🔔 NOTIFICACIÓN: ✅ Notificación $notificacionId marcada como leída")
        }
    }
    
    // Función para obtener notificaciones no leídas
    fun obtenerNotificacionesNoLeidas(): List<NotificacionReal> {
        return _notificacionesCompartidas.value.filter { !it.leida }
    }
    
    // Función para limpiar notificaciones leídas
    fun limpiarNotificacionesLeidas() {
        val notificacionesActuales = _notificacionesCompartidas.value.toMutableList()
        notificacionesActuales.removeAll { it.leida }
        _notificacionesCompartidas.value = notificacionesActuales
        println("🔔 NOTIFICACIÓN: ✅ Notificaciones leídas eliminadas")
    }
    
    // Función para obtener el número de notificaciones no leídas
    fun obtenerNumeroNotificacionesNoLeidas(): Int {
        return _notificacionesCompartidas.value.count { !it.leida }
    }
    
    // Función para forzar actualización de notificaciones
    fun forzarActualizacionNotificaciones() {
        println("🔔 NOTIFICACIÓN: Forzando actualización de notificaciones")
        println("🔔 NOTIFICACIÓN: Total actual: ${_notificacionesCompartidas.value.size}")
        _notificacionesCompartidas.value = _notificacionesCompartidas.value.toList()
    }
    
    // Función para agregar notificación de prueba
    fun agregarNotificacionPrueba() {
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val notificacionPrueba = NotificacionReal(
            id = System.currentTimeMillis().toInt(),
            equipoId = 8,
            equipoNombre = "Equipo 008",
            equipoCodigo = "008",
            ubicacion = "Sala de Emergencias",
            reportadoPor = "Dr. María González",
            fecha = fechaActual,
            descripcion = "Notificación de prueba - Equipo con fallas",
            leida = false
        )
        
        val notificacionesActuales = _notificacionesCompartidas.value.toMutableList()
        notificacionesActuales.add(0, notificacionPrueba)
        _notificacionesCompartidas.value = notificacionesActuales
        
        println("🔔 NOTIFICACIÓN: ✅ Notificación de prueba agregada")
        println("🔔 NOTIFICACIÓN: Total después de agregar: ${_notificacionesCompartidas.value.size}")
    }
} 