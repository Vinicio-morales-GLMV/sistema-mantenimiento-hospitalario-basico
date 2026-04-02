package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.MetricasEquipo
import com.example.myapplication.MetricasViewModel

@Composable
fun MetricasScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    rolUsuario: String = ""
) {
    val viewModel: MetricasViewModel = viewModel()
    val metricas by viewModel.metricas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Debug: Imprimir estado actual
    LaunchedEffect(metricas, isLoading, error) {
        println("🔍 MÉTRICAS: Estado actualizado")
        println("🔍 MÉTRICAS: Métricas: ${metricas.size}")
        println("🔍 MÉTRICAS: Loading: $isLoading")
        println("🔍 MÉTRICAS: Error: $error")
    }

    // Cargar métricas al iniciar
    LaunchedEffect(Unit) {
        println("🔍 MÉTRICAS: Iniciando carga de métricas")
        viewModel.cargarMetricas()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Métricas de Mantenimiento",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MTTR, MTBF, Disponibilidad, Confiabilidad, OEE",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total: ${metricas.size} equipos analizados",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onBackToMenu,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        "Volver al menú principal",
                        color = Color(0xFF1976D2),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Contenido
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF1976D2))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Calculando métricas...", color = Color.Gray)
                    }
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error: $error",
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { 
                                println("🔍 MÉTRICAS: Reintentando carga")
                                viewModel.cargarMetricas() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }
            metricas.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No hay métricas disponibles",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { 
                                println("🔍 MÉTRICAS: Cargando métricas")
                                viewModel.cargarMetricas() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Cargar Métricas", color = Color.White)
                        }
                    }
                }
            }
            else -> {
                // Lista de métricas
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(metricas) { metrica ->
                        println("🔍 MÉTRICAS: Mostrando métricas para: ${metrica.equipoNombre}")
                        MetricaCard(metrica = metrica)
                    }
                }
            }
        }
    }
}

@Composable
fun MetricaCard(metrica: MetricasEquipo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con nombre del equipo
            Text(
                text = metrica.equipoNombre.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp),
                softWrap = true,
                maxLines = 2
            )

            // Métricas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Columna izquierda
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    MetricaItem("MTTR", "${String.format("%.2f", metrica.mttr)} hrs", Color(0xFFFF5722))
                    MetricaItem("MTBF", "${String.format("%.2f", metrica.mtbf)} hrs", Color(0xFF4CAF50))
                    MetricaItem("Fallas", "${metrica.fallas}", Color(0xFF9C27B0))
                }
                
                // Columna derecha
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    MetricaItem("Disponibilidad", "${String.format("%.1f", metrica.disponibilidad)}%", Color(0xFF2196F3))
                    MetricaItem("Confiabilidad", "${String.format("%.1f", metrica.confiabilidad)}%", Color(0xFFFF9800))
                    MetricaItem("OEE", "${String.format("%.1f", metrica.oee)}%", Color(0xFF607D8B))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tiempo Operación: ${String.format("%.1f", metrica.tiempoOperacion)} hrs",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Tiempo Reparación: ${String.format("%.1f", metrica.tiempoReparacion)} hrs",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MetricaItem(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
} 