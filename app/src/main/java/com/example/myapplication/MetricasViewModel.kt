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
import java.util.concurrent.TimeUnit
import kotlin.math.exp

data class MetricasEquipo(
    val equipoId: Int,
    val equipoNombre: String,
    val mttr: Double,
    val mtbf: Double,
    val disponibilidad: Double,
    val confiabilidad: Double,
    val oee: Double,
    val fallas: Int,
    val tiempoOperacion: Double,
    val tiempoReparacion: Double
)

class MetricasViewModel : ViewModel() {
    
    private val _metricas = MutableStateFlow<List<MetricasEquipo>>(emptyList())
    val metricas: StateFlow<List<MetricasEquipo>> = _metricas
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun cargarMetricas() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val metricasCalculadas = obtenerMetricasDirecto()
                _metricas.value = metricasCalculadas
                println("🔍 MÉTRICAS: Cargadas ${metricasCalculadas.size} métricas")
            } catch (e: Exception) {
                _error.value = "Error al cargar métricas: ${e.message}"
                println("🔍 MÉTRICAS: Error - ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun obtenerMetricasDirecto(): List<MetricasEquipo> = withContext(Dispatchers.IO) {
        try {
            println("🔍 MÉTRICAS: Iniciando cálculo de métricas")
            
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
            
            // Obtener datos de mantenimientos con información de equipos
            val url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/mantenimientos?select=*,instrumentos(nombre)&order=equipo_id"
            println("🔍 MÉTRICAS: URL: $url")
            
            val request = Request.Builder()
                .url(url)
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                throw Exception("Error HTTP: ${response.code} - ${response.message}")
            }
            
            val responseBody = response.body?.string()
            println("🔍 MÉTRICAS: Respuesta recibida: ${responseBody?.length ?: 0} caracteres")
            
            if (responseBody.isNullOrEmpty()) {
                throw Exception("Respuesta vacía del servidor")
            }
            
            val jsonArray = JSONArray(responseBody)
            println("🔍 MÉTRICAS: ${jsonArray.length()} registros de mantenimiento encontrados")
            
            // Agrupar datos por equipo
            val datosPorEquipo = mutableMapOf<Int, MutableList<JSONObject>>()
            
            for (i in 0 until jsonArray.length()) {
                val mantenimiento = jsonArray.getJSONObject(i)
                val equipoId = mantenimiento.getInt("equipo_id")
                
                if (!datosPorEquipo.containsKey(equipoId)) {
                    datosPorEquipo[equipoId] = mutableListOf()
                }
                datosPorEquipo[equipoId]?.add(mantenimiento)
            }
            
            // Calcular métricas para cada equipo
            val metricasCalculadas = mutableListOf<MetricasEquipo>()
            
            datosPorEquipo.forEach { (equipoId, mantenimientos) ->
                val equipoNombre = mantenimientos.firstOrNull()?.getJSONObject("instrumentos")?.getString("nombre") ?: "Equipo $equipoId"
                
                // Calcular métricas usando datos disponibles
                val metricas = calcularMetricasParaEquipo(mantenimientos)
                
                metricasCalculadas.add(
                    MetricasEquipo(
                        equipoId = equipoId,
                        equipoNombre = equipoNombre,
                        mttr = metricas.mttr,
                        mtbf = metricas.mtbf,
                        disponibilidad = metricas.disponibilidad,
                        confiabilidad = metricas.confiabilidad,
                        oee = metricas.oee,
                        fallas = metricas.fallas,
                        tiempoOperacion = metricas.tiempoOperacion,
                        tiempoReparacion = metricas.tiempoReparacion
                    )
                )
            }
            
            println("🔍 MÉTRICAS: Calculadas ${metricasCalculadas.size} métricas")
            metricasCalculadas
            
        } catch (e: Exception) {
            println("🔍 MÉTRICAS: Error en obtenerMetricasDirecto: ${e.message}")
            throw e
        }
    }
    
    private fun calcularMetricasParaEquipo(mantenimientos: List<JSONObject>): MetricasCalculadas {
        // Datos de ejemplo si no hay suficientes datos reales
        val fallas = mantenimientos.size
        val tiempoReparacion = mantenimientos.sumOf { 
            it.optDouble("duracion_reparacion", 2.5) // Valor por defecto si no existe
        }
        val tiempoOperacion = mantenimientos.sumOf { 
            it.optDouble("tiempo_funcionamiento", 100.0) // Valor por defecto si no existe
        }
        
        // Calcular métricas básicas
        val mttr = if (fallas > 0) tiempoReparacion / fallas else 0.0
        val mtbf = if (fallas > 0) tiempoOperacion / fallas else 0.0
        
        // Calcular disponibilidad (MTBF / (MTBF + MTTR)) * 100
        val disponibilidad = if (mtbf + mttr > 0) (mtbf / (mtbf + mttr)) * 100 else 0.0
        
        // Calcular confiabilidad (e^(-tiempo/MTBF)) - usando tiempo de operación como referencia
        val confiabilidad = if (mtbf > 0) exp(-tiempoOperacion / mtbf) * 100 else 0.0
        
        // Calcular OEE (Disponibilidad * Rendimiento * Calidad)
        // Por ahora usamos valores por defecto para rendimiento y calidad
        val rendimiento = 85.0 // Valor por defecto
        val calidad = 95.0 // Valor por defecto
        val oee = (disponibilidad * rendimiento * calidad) / 10000 // Dividir por 10000 porque cada métrica está en porcentaje
        
        return MetricasCalculadas(
            mttr = mttr,
            mtbf = mtbf,
            disponibilidad = disponibilidad,
            confiabilidad = confiabilidad,
            oee = oee,
            fallas = fallas,
            tiempoOperacion = tiempoOperacion,
            tiempoReparacion = tiempoReparacion
        )
    }
}

data class MetricasCalculadas(
    val mttr: Double,
    val mtbf: Double,
    val disponibilidad: Double,
    val confiabilidad: Double,
    val oee: Double,
    val fallas: Int,
    val tiempoOperacion: Double,
    val tiempoReparacion: Double
) 