package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

data class Mantenimiento(
    val id: Int,
    val equipoId: Int,
    val equipoNombre: String?,
    val fechaMantenimiento: String,
    val descripcion: String,
    val archivoUrl: String?,
    val tipoMantenimiento: String?,
    val tecnicoResponsable: String?,
    val costo: Double?,
    val estado: String?
)

class MantenimientoViewModel : ViewModel() {
    
    private val _mantenimientos = MutableStateFlow<List<Mantenimiento>>(emptyList())
    val mantenimientos: StateFlow<List<Mantenimiento>> = _mantenimientos
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarMantenimientos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val mantenimientosList = obtenerMantenimientosDirecto()
                _mantenimientos.value = mantenimientosList
                println("🔍 MANTENIMIENTO: Cargados ${mantenimientosList.size} mantenimientos")
            } catch (e: Exception) {
                _error.value = "Error al cargar mantenimientos: ${e.message}"
                println("🔍 MANTENIMIENTO: Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun obtenerMantenimientosDirecto(): List<Mantenimiento> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        
        // Query para obtener mantenimientos con información del equipo
        val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/mantenimientos?select=*,instrumentos(nombre)&order=fecha_mantenimiento.desc"
        
        val request = Request.Builder()
            .url(url)
            .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .get()
            .build()

        val response = client.newCall(request).execute()
        
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            println("🔍 MANTENIMIENTO: Respuesta de Supabase: $responseBody")
            
            if (responseBody != null) {
                val jsonArray = JSONArray(responseBody)
                val mantenimientosList = mutableListOf<Mantenimiento>()
                
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    
                    // Obtener información del equipo
                    val equipoInfo = obj.optJSONObject("instrumentos")
                    val equipoNombre = equipoInfo?.optString("nombre")
                    
                    val mantenimiento = Mantenimiento(
                        id = obj.getInt("id"),
                        equipoId = obj.getInt("equipo_id"),
                        equipoNombre = equipoNombre,
                        fechaMantenimiento = obj.getString("fecha_mantenimiento"),
                        descripcion = obj.getString("descripcion"),
                        archivoUrl = obj.optString("archivo_url").takeIf { it != "null" && it.isNotEmpty() },
                        tipoMantenimiento = obj.optString("tipo_mantenimiento").takeIf { it != "null" && it.isNotEmpty() },
                        tecnicoResponsable = obj.optString("tecnico_responsable").takeIf { it != "null" && it.isNotEmpty() },
                        costo = obj.optDouble("costo").takeIf { !it.isNaN() },
                        estado = obj.optString("estado").takeIf { it != "null" && it.isNotEmpty() }
                    )
                    
                    mantenimientosList.add(mantenimiento)
                }
                
                return@withContext mantenimientosList
            } else {
                throw Exception("Respuesta vacía del servidor")
            }
        } else {
            val errorBody = response.body?.string()
            println("🔍 MANTENIMIENTO: Error HTTP ${response.code}: $errorBody")
            throw Exception("Error HTTP ${response.code}: $errorBody")
        }
    }
} 