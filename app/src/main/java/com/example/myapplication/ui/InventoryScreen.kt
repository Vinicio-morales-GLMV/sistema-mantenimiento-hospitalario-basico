package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InventoryScreen(
    onBackToMenu: () -> Unit,
    nombreUsuario: String = "",
    onRegistrarUsuario: (() -> Unit)? = null,
    rolUsuario: String = ""
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título principal
        Text(
            text = "Pantalla de Inventario de Equipos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Subtítulo
        Text(
            text = "(próxima versión)",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Botón
        Button(
            onClick = onBackToMenu,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Volver al menú principal", color = Color.White)
        }
    }
} 