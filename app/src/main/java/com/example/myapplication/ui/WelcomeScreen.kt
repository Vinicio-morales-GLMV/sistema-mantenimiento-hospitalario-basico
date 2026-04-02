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

val HospitalBlue = Color(0xFF0D47A1) // Azul hospital más oscuro
val HospitalBlueLight = Color(0xFF90CAF9) // Azul claro más oscuro

@Composable
fun WelcomeScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HospitalBlueLight)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(bottom = 70.dp)
        ) {
            Text(
                text = "Bienvenido a",
                color = HospitalBlue,
                fontSize = 40.sp, // antes 32.sp
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "HospiTec",
                color = HospitalBlue,
                fontSize = 50.sp, // antes 40.sp
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(65.dp), // antes 52.dp
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text("Iniciar sesión", fontSize = 23.sp, color = HospitalBlue) // antes 18.sp
        }
    }
} 