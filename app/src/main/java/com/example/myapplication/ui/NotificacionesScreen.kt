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
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.EquipoViewModel
import com.example.myapplication.NotificacionReal

@Composable
fun NotificacionesScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    rolUsuario: String = ""
) {
    val viewModel: EquipoViewModel = viewModel()
    val notificaciones by viewModel.notificaciones.collectAsState()
    
    // Datos de prueba temporales mientras se resuelve la sincronización
    var notificacionesConPruebas by remember { mutableStateOf(listOf<NotificacionReal>()) }
    
    LaunchedEffect(Unit) {
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        notificacionesConPruebas = listOf(
            NotificacionReal(
                id = 1,
                equipoId = 8,
                equipoNombre = "Equipo 008",
                equipoCodigo = "008",
                ubicacion = "Sala de Emergencias",
                reportadoPor = "Dr. María González",
                fecha = fechaActual,
                descripcion = "El equipo presenta fallas en el sistema de monitoreo",
                leida = false
            ),
            NotificacionReal(
                id = 2,
                equipoId = 3,
                equipoNombre = "Ventilador Mecánico",
                equipoCodigo = "VENT-003",
                ubicacion = "UCI",
                reportadoPor = "Dr. Carlos Ruiz",
                fecha = fechaActual,
                descripcion = "El ventilador presenta ruidos anormales durante la operación",
                leida = false
            )
        )
    }
    
    // Combinar notificaciones reales con las de prueba
    val notificacionesFinales = if (notificaciones.isNotEmpty()) {
        notificaciones
    } else {
        notificacionesConPruebas
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
                    text = "Notificaciones de Equipos",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Alertas de equipos sin funcionar",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón para volver al menú
                Button(
                    onClick = onBackToMenu,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Volver al menú principal", color = Color.White)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botón de prueba para agregar notificación
                Button(
                    onClick = { 
                        viewModel.agregarNotificacionPrueba()
                        viewModel.forzarActualizacionNotificaciones()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text("➕ Agregar Notificación de Prueba", color = Color.White)
                }
            }
        }

        // Lista de notificaciones
        if (notificacionesFinales.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📭 No hay notificaciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Todas las notificaciones aparecerán aquí cuando los doctores reporten equipos sin funcionar",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(notificacionesFinales) { notificacion ->
                    NotificacionCard(notificacion = notificacion, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun NotificacionCard(
    notificacion: NotificacionReal,
    viewModel: EquipoViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con ícono de alerta
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "🚨",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Text(
                    text = "ALERTA DE EQUIPO SIN FUNCIONAR",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Información del equipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Equipo:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = notificacion.equipoNombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Código:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = notificacion.equipoCodigo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Ubicación y reportado por
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Ubicación:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = notificacion.ubicacion,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reportado por:",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = notificacion.reportadoPor,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Fecha
            Text(
                text = "Fecha: ${notificacion.fecha}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            
            // Descripción del problema
            if (!notificacion.descripcion.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Descripción del problema:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = notificacion.descripcion,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { 
                        viewModel.marcarNotificacionComoLeida(notificacion.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Marcar como leída", color = Color.White, fontSize = 12.sp)
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = { /* Ver detalles del equipo - TODO: Implementar navegación */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver equipo", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
} 