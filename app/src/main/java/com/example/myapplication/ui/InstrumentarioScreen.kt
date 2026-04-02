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
import kotlinx.coroutines.launch

@Composable
fun InstrumentarioScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    rolUsuario: String = ""
) {
    val viewModel: EquipoViewModel = viewModel()
    val equipos by viewModel.equipos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Configuración de permisos
    val isDoctor = rolUsuario == "doctor"
    val isUsuario = rolUsuario == "usuario"
    val isAdmin = rolUsuario == "admin" || rolUsuario == "Administrador" || rolUsuario == "Administrador e Ingeniero biomédico"
    val puedeEditar = !isUsuario // Los usuarios no pueden editar
    val esDoctor = isDoctor // Los doctores solo pueden cambiar a "sinfuncionar"
    val puedeAgregarEquipo = isAdmin // Solo administradores pueden agregar equipos

    // Estado para controlar el diálogo
    var showAgregarEquipoDialog by remember { mutableStateOf(false) }

    // Debug: Imprimir estado actual
    LaunchedEffect(equipos, isLoading, error) {
        println("🔍 INSTRUMENTARIO: Estado actualizado")
        println("🔍 INSTRUMENTARIO: Equipos: ${equipos.size}")
        println("🔍 INSTRUMENTARIO: Loading: $isLoading")
        println("🔍 INSTRUMENTARIO: Error: $error")
        println("🔍 INSTRUMENTARIO: Rol: $rolUsuario")
        println("🔍 INSTRUMENTARIO: Puede editar: $puedeEditar")
        println("🔍 INSTRUMENTARIO: Es doctor: $esDoctor")
        println("🔍 INSTRUMENTARIO: Puede agregar equipo: $puedeAgregarEquipo")
    }

    // Cargar equipos al iniciar
    LaunchedEffect(Unit) {
        println("🔍 INSTRUMENTARIO: Iniciando carga de equipos")
        viewModel.cargarEquipos()
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
                    text = "Instrumentario Médico",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botón "Agregar otro equipo" solo para administradores
                if (puedeAgregarEquipo) {
                    Button(
                        onClick = { showAgregarEquipoDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(
                            "➕ Agregar otro equipo",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
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
                        Text("Cargando instrumentos...", color = Color.Gray)
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
                                println("🔍 INSTRUMENTARIO: Reintentando carga")
                                viewModel.cargarEquipos() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }
            equipos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No hay instrumentos disponibles",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Button(
                            onClick = { 
                                println("🔍 INSTRUMENTARIO: Cargando instrumentos")
                                viewModel.cargarEquipos() 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text("Cargar Instrumentos", color = Color.White)
                        }
                    }
                }
            }
            else -> {
                // Lista de instrumentos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(equipos) { equipo ->
                        println("🔍 INSTRUMENTARIO: Mostrando equipo: ${equipo.nombre}")
                        InstrumentoCard(
                            equipo = equipo,
                            puedeEditar = puedeEditar,
                            esDoctor = esDoctor,
                            nombreUsuario = nombreUsuario,
                            onEstadoChange = { nuevoEstado, descripcionProblema ->
                                println("🔍 INSTRUMENTARIO: Cambiando estado de ${equipo.nombre} a $nuevoEstado")
                                equipo.id?.let { id ->
                                    coroutineScope.launch {
                                        viewModel.actualizarEstadoEquipo(id, nuevoEstado, descripcionProblema, nombreUsuario)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo para agregar nuevo equipo (solo para administradores)
    if (puedeAgregarEquipo && showAgregarEquipoDialog) {
        AgregarEquipoDialog(
            onDismiss = { showAgregarEquipoDialog = false },
            onEquipoCreado = { nuevoEquipo ->
                println("🔍 INSTRUMENTARIO: Nuevo equipo creado: ${nuevoEquipo.nombre}")
                viewModel.cargarEquipos() // Recargar la lista
                showAgregarEquipoDialog = false
            },
            nombreUsuario = nombreUsuario,
            viewModel = viewModel
        )
    }
}

@Composable
fun AgregarEquipoDialog(
    onDismiss: () -> Unit,
    onEquipoCreado: (Equipo) -> Unit,
    nombreUsuario: String,
    viewModel: EquipoViewModel
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var serie by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var fechaAdquisicion by remember { mutableStateOf("") }
    var proximoMantenimiento by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text("Agregar nuevo equipo") 
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código del equipo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del equipo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de equipo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = marca,
                    onValueChange = { marca = it },
                    label = { Text("Marca *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = modelo,
                    onValueChange = { modelo = it },
                    label = { Text("Modelo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = serie,
                    onValueChange = { serie = it },
                    label = { Text("Número de serie *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicación *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = fechaAdquisicion,
                    onValueChange = { fechaAdquisicion = it },
                    label = { Text("Fecha de adquisición (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = proximoMantenimiento,
                    onValueChange = { proximoMantenimiento = it },
                    label = { Text("Próximo mantenimiento (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (codigo.isNotBlank() && nombre.isNotBlank() && tipo.isNotBlank() && 
                        marca.isNotBlank() && modelo.isNotBlank() && serie.isNotBlank() && ubicacion.isNotBlank()) {
                        
                        viewModel.crearNuevoEquipo(
                            codigo = codigo,
                            nombre = nombre,
                            tipo = tipo,
                            marca = marca,
                            modelo = modelo,
                            serie = serie,
                            ubicacion = ubicacion,
                            fechaAdquisicion = fechaAdquisicion,
                            proximoMantenimiento = proximoMantenimiento,
                            nombreUsuario = nombreUsuario
                        )
                        
                        // Limpiar campos
                        codigo = ""
                        nombre = ""
                        tipo = ""
                        marca = ""
                        modelo = ""
                        serie = ""
                        ubicacion = ""
                        fechaAdquisicion = ""
                        proximoMantenimiento = ""
                        
                        onDismiss()
                    }
                },
                enabled = codigo.isNotBlank() && nombre.isNotBlank() && tipo.isNotBlank() && 
                         marca.isNotBlank() && modelo.isNotBlank() && serie.isNotBlank() && ubicacion.isNotBlank()
            ) {
                Text("Crear equipo")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun InstrumentoCard(
    equipo: Equipo,
    puedeEditar: Boolean,
    esDoctor: Boolean,
    nombreUsuario: String,
    onEstadoChange: (String, String?) -> Unit
) {
    var showEstadoDialog by remember { mutableStateOf(false) }
    var selectedEstado by remember { mutableStateOf(equipo.estado ?: "Funcionando") }
    var showDescripcionDialog by remember { mutableStateOf(false) }
    var descripcionProblema by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con código y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Código del equipo
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = equipo.codigo ?: "N/A",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                // Estado del equipo con colores
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Determinar color según estado
                    val estadoColor = when (equipo.estado) {
                        "Sin funcionar" -> Color.Red
                        "En mantenimiento" -> Color(0xFFFFA500) // Amarillo
                        else -> Color.Green
                    }
                    
                    // Indicador de estado (círculo con color)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .padding(end = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = estadoColor),
                            modifier = Modifier.size(12.dp)
                        ) {}
                    }
                    Text(
                        text = when (equipo.estado) {
                            "Funcionando" -> "En funcionamiento"
                            "En mantenimiento" -> "En mantenimiento"
                            "Sin funcionar" -> "Sin funcionar"
                            else -> equipo.estado ?: "Sin estado"
                        },
                        color = estadoColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre del equipo
            Text(
                text = equipo.nombre?.uppercase() ?: "N/A",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp),
                softWrap = true,
                maxLines = 3
            )

            // Especificaciones detalladas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Columna izquierda
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    EspecificacionItem("TIPO", equipo.tipo ?: "N/A")
                    EspecificacionItem("MODELO", equipo.modelo ?: "N/A")
                    EspecificacionItem("UBICACIÓN", equipo.ubicacion ?: "N/A")
                }
                
                // Columna derecha
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    EspecificacionItem("MARCA", equipo.marca ?: "N/A")
                    EspecificacionItem("SERIE", equipo.serie ?: "N/A")
                    EspecificacionItem("RESPONSABLE", equipo.responsable ?: "N/A")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Línea divisoria
            Divider(color = Color.LightGray, thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(12.dp))

            // Sección de cambio de estado - Solo visible si puede editar
            if (puedeEditar) {
                Text(
                    text = "Cambiar estado:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Button(
                    onClick = { 
                        println("🔍 INSTRUMENTARIO: Abriendo dialog para ${equipo.nombre}")
                        showEstadoDialog = true 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Seleccionar nuevo estado", color = Color.White)
                }
            } else {
                // Mensaje para usuarios sin permisos de edición
                Text(
                    text = "Solo visualización - Sin permisos de edición",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    // Dialog para seleccionar estado
    if (showEstadoDialog) {
        AlertDialog(
            onDismissRequest = { showEstadoDialog = false },
            title = { 
                Text(
                    if (esDoctor) "Cambiar a Sin Funcionar" 
                    else "Seleccionar Estado"
                ) 
            },
            text = {
                Column {
                    val estados = if (esDoctor) {
                        // Los doctores solo pueden cambiar a "Sin funcionar"
                        listOf("Sin funcionar")
                    } else {
                        // Otros roles pueden cambiar a cualquier estado
                        listOf("Funcionando", "En mantenimiento", "Sin funcionar")
                    }
                    
                    estados.forEach { estado ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedEstado == estado,
                                onClick = { selectedEstado = estado }
                            )
                            Text(
                                text = estado,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
                    // Mensaje informativo para doctores
                    if (esDoctor) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Como doctor, solo puedes marcar equipos como 'Sin funcionar'",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        println("🔍 INSTRUMENTARIO: Confirmando cambio de estado a $selectedEstado")
                        if (esDoctor && selectedEstado == "Sin funcionar") {
                            // Si es doctor y está cambiando a "sin funcionar", mostrar diálogo de descripción
                            showDescripcionDialog = true
                        } else {
                            // Para otros roles o estados, proceder normalmente
                            selectedEstado?.let { estado ->
                                onEstadoChange(estado, null)
                            }
                            showEstadoDialog = false
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showEstadoDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog para descripción del problema (solo para doctores)
    if (showDescripcionDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDescripcionDialog = false
                showEstadoDialog = false
            },
            title = { 
                Text("Descripción del problema") 
            },
            text = {
                Column {
                    Text(
                        text = "Por favor, describe el problema que detectaste con el equipo:",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = descripcionProblema,
                        onValueChange = { descripcionProblema = it },
                        label = { Text("Descripción del problema") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (descripcionProblema.isNotBlank()) {
                            selectedEstado?.let { estado ->
                                onEstadoChange(estado, descripcionProblema)
                            }
                            showDescripcionDialog = false
                            showEstadoDialog = false
                            descripcionProblema = ""
                        }
                    },
                    enabled = descripcionProblema.isNotBlank()
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { 
                        showDescripcionDialog = false
                        showEstadoDialog = false
                        descripcionProblema = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun EspecificacionItem(label: String, value: String) {
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
            maxLines = 2
        )
    }
} 