package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon

@Composable
fun MenuGeneralScreen(
    nombre: String,
    rol: String,
    onInstrumentario: (() -> Unit)? = null,
    onMantenimiento: (() -> Unit)? = null,
    onMetricas: (() -> Unit)? = null,
    onRegistrarUsuario: (() -> Unit)? = null,
    onLogout: () -> Unit,
    onGestionarUsuarios: (() -> Unit)? = null,
    onNotificaciones: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Hola, $nombre!",
            color = HospitalBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "Rol: $rol",
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        if (onInstrumentario != null) {
            Button(
                onClick = onInstrumentario,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.MedicalServices, contentDescription = "Instrumentario", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Instrumentario", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        if (onMantenimiento != null) {
            Button(
                onClick = onMantenimiento,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.Build, contentDescription = "Mantenimiento", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Mantenimiento", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        if (onMetricas != null) {
            Button(
                onClick = onMetricas,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.Analytics, contentDescription = "Métricas", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Métricas de Mantenimiento", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        if (onNotificaciones != null) {
            Button(
                onClick = onNotificaciones,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Notificaciones", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        if (onRegistrarUsuario != null) {
            Button(
                onClick = onRegistrarUsuario,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 32.dp), // Más espacio debajo
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Registrar usuario", tint = HospitalBlue, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Registrar nuevo usuario", fontSize = 22.sp, color = HospitalBlue, fontWeight = FontWeight.SemiBold)
            }
        }
        // Botón Gestionar usuarios solo para usuarios específicos
        val puedeGestionarUsuarios = nombre in listOf("Santiago Nuñez", "Gabriel Leon", "Kerly Verdezoto")
        if (puedeGestionarUsuarios && onGestionarUsuarios != null) {
            Button(
                onClick = onGestionarUsuarios,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HospitalBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(bottom = 18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Filled.Group, contentDescription = "Gestionar usuarios", tint = Color.White, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(16.dp))
                Text("Gestionar usuarios", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        Button(
            onClick = onLogout,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 10.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Text("Cerrar sesión", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
} 