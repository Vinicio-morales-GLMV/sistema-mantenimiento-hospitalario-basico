package com.example.myapplication.ui

data class Usuario(val nombre: String, val correo: String, val contrasena: String, val rol: String)

val usuarios = listOf(
    Usuario("Santiago Nuñez", "santiago.nunez@hospitec.com", "santiago1234", "jefe_hospital"),
    Usuario("Gabriel Leon", "gabriel.leon@hospitec.com", "gabriel1234", "admin"),
    Usuario("Kerly Verdezoto", "kerly.verdezoto@hospitec.com", "kerly1234", "admin"),
    Usuario("Amy Aguaguiña", "amy.aguagina@hospitec.com", "amy1234", "tecnico"),
    Usuario("Milena Silva", "milena.silva@hospitec.com", "milena1234", "doctor"),
    Usuario("Sebastian Salas", "sebastian.salas@hospitec.com", "sebastian1234", "usuario")
) 