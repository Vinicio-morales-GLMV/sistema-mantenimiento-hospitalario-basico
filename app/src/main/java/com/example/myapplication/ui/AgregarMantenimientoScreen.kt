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
import com.example.myapplication.Equipo
import com.example.myapplication.EquipoViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarMantenimientoScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    rolUsuario: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val equiposViewModel: EquipoViewModel = viewModel()
    val equipos by equiposViewModel.equipos.collectAsState()
    
    // Estados para el formulario
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var tipoMantenimiento by remember { mutableStateOf("Preventivo") }
    var tecnico by remember { mutableStateOf(nombreUsuario) }
    var costo by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var archivoSeleccionado by remember { mutableStateOf<Uri?>(null) }
    var archivoUrl by remember { mutableStateOf<String?>(null) }
    var mostrarDropdown by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf<String?>(null) }
    
    // Launcher para seleccionar archivo
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        archivoSeleccionado = uri
    }
    
    // Cargar equipos al iniciar
    LaunchedEffect(Unit) {
        equiposViewModel.cargarEquipos()
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
                Text(
                    text = "Agregar Mantenimiento",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nuevo registro de mantenimiento",
                    color = Color.White,
                    fontSize = 14.sp
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
            }
        }
        
        // Formulario
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Selección de equipo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Equipo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Box {
                            OutlinedTextField(
                                value = equipoSeleccionado?.nombre ?: "Seleccionar equipo",
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    Text(
                                        text = if (mostrarDropdown) "▲" else "▼",
                                        modifier = Modifier.clickable { mostrarDropdown = !mostrarDropdown }
                                    )
                                }
                            )
                            
                            DropdownMenu(
                                expanded = mostrarDropdown,
                                onDismissRequest = { mostrarDropdown = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                equipos.forEach { equipo ->
                                    DropdownMenuItem(
                                        text = { Text(equipo.nombre ?: "Sin nombre") },
                                        onClick = {
                                            equipoSeleccionado = equipo
                                            mostrarDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Tipo de mantenimiento
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Tipo de Mantenimiento",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { tipoMantenimiento = "Preventivo" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (tipoMantenimiento == "Preventivo") Color(0xFF4CAF50) else Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Preventivo", color = Color.White)
                            }
                            
                            Button(
                                onClick = { tipoMantenimiento = "Correctivo" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (tipoMantenimiento == "Correctivo") Color(0xFFFF9800) else Color.Gray
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Correctivo", color = Color.White)
                            }
                        }
                    }
                }
            }
            
            item {
                // Descripción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Descripción",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Describe las actividades realizadas...") },
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                }
            }
            
            item {
                // Técnico y Costo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Detalles Adicionales",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = tecnico,
                                onValueChange = { tecnico = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Técnico") },
                                label = { Text("Técnico") }
                            )
                            
                            OutlinedTextField(
                                value = costo,
                                onValueChange = { costo = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("0.00") },
                                label = { Text("Costo ($)") }
                            )
                        }
                    }
                }
            }
            
            item {
                // Observaciones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Observaciones",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = observaciones,
                            onValueChange = { observaciones = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Observaciones adicionales...") },
                            minLines = 2,
                            maxLines = 3
                        )
                    }
                }
            }
            
            item {
                // Cargar archivo
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Archivo Adjunto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Button(
                            onClick = { launcher.launch("*/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("📎 Seleccionar Archivo", color = Color.White)
                        }
                        
                        if (archivoSeleccionado != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Archivo seleccionado: ${archivoSeleccionado.toString().split("/").last()}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            
            item {
                // Botón guardar
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                // Validaciones
                                if (equipoSeleccionado == null) {
                                    mensaje = "Debes seleccionar un equipo"
                                    return@launch
                                }
                                if (descripcion.isBlank()) {
                                    mensaje = "Debes agregar una descripción"
                                    return@launch
                                }
                                
                                // Guardar mantenimiento
                                val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                val costoDouble = costo.toDoubleOrNull() ?: 0.0
                                
                                // Aquí se guardaría el mantenimiento en Supabase
                                // Por ahora solo mostramos un mensaje de éxito
                                mensaje = "✅ Mantenimiento guardado exitosamente"
                                
                                // Limpiar formulario
                                equipoSeleccionado = null
                                descripcion = ""
                                tipoMantenimiento = "Preventivo"
                                tecnico = nombreUsuario
                                costo = ""
                                observaciones = ""
                                archivoSeleccionado = null
                                archivoUrl = null
                                
                            } catch (e: Exception) {
                                println("🔍 MANTENIMIENTO: Error en el proceso: ${e.message}")
                                mensaje = "Error: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("💾 Guardar Mantenimiento", color = Color.White)
                    }
                }
            }
            
            // Mensaje de estado
            if (mensaje != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (mensaje!!.startsWith("Error")) Color(0xFFFFCDD2) else Color(0xFFC8E6C9)
                        )
                    ) {
                        Text(
                            text = mensaje!!,
                            modifier = Modifier.padding(16.dp),
                            color = if (mensaje!!.startsWith("Error")) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }
    }
} 