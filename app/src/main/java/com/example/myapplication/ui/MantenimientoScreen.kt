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
import com.example.myapplication.Mantenimiento
import com.example.myapplication.MantenimientoViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun MantenimientoScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    rolUsuario: String = "",
    onAgregarMantenimiento: () -> Unit = {}
) {
    val viewModel: MantenimientoViewModel = viewModel()
    val mantenimientos by viewModel.mantenimientos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    // Debug: Imprimir estado actual
    LaunchedEffect(mantenimientos, isLoading, error) {
        println("🔍 MANTENIMIENTO: Estado actualizado")
        println("🔍 MANTENIMIENTO: Mantenimientos: ${mantenimientos.size}")
        println("🔍 MANTENIMIENTO: Loading: $isLoading")
        println("🔍 MANTENIMIENTO: Error: $error")
    }

    // Cargar mantenimientos al iniciar
    LaunchedEffect(Unit) {
        println("🔍 MANTENIMIENTO: Iniciando carga de mantenimientos")
        viewModel.cargarMantenimientos()
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
                    text = "Registro de Mantenimientos",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total: ${mantenimientos.size} mantenimientos",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón para agregar mantenimiento
                Button(
                    onClick = onAgregarMantenimiento,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(
                        "➕ Agregar Mantenimiento",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
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
                        Text("Cargando mantenimientos...", color = Color.Gray)
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
                                println("🔍 MANTENIMIENTO: Reintentando carga")
                                viewModel.cargarMantenimientos() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }
            mantenimientos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No hay mantenimientos registrados",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { 
                                println("🔍 MANTENIMIENTO: Cargando mantenimientos")
                                viewModel.cargarMantenimientos() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Cargar Mantenimientos", color = Color.White)
                        }
                    }
                }
            }
            else -> {
                // Lista de mantenimientos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mantenimientos) { mantenimiento ->
                        println("🔍 MANTENIMIENTO: Mostrando mantenimiento: ${mantenimiento.id}")
                        MantenimientoCard(
                            mantenimiento = mantenimiento,
                            onArchivoClick = { url ->
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MantenimientoCard(
    mantenimiento: Mantenimiento,
    onArchivoClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con ID y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ID del mantenimiento
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "MNT-${mantenimiento.id.toString().padStart(3, '0')}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Fecha del mantenimiento
                Text(
                    text = mantenimiento.fechaMantenimiento,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del equipo
            Text(
                text = mantenimiento.equipoNombre?.uppercase() ?: "Equipo no encontrado",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp),
                softWrap = true,
                maxLines = 2
            )

            // Descripción
            Text(
                text = mantenimiento.descripcion,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp),
                softWrap = true,
                maxLines = 3
            )

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Columna izquierda
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    InfoItem("TIPO", mantenimiento.tipoMantenimiento ?: "No especificado")
                    InfoItem("TÉCNICO", mantenimiento.tecnicoResponsable ?: "No asignado")
                }
                
                // Columna derecha
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    InfoItem("ESTADO", mantenimiento.estado ?: "Completado")
                    InfoItem("COSTO", if (mantenimiento.costo != null) "$${mantenimiento.costo}" else "No registrado")
                }
            }

            // Archivo adjunto
            mantenimiento.archivoUrl?.let { url ->
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { onArchivoClick(url) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("📎 Ver Archivo Adjunto", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            softWrap = true,
            maxLines = 1
        )
    }
} 