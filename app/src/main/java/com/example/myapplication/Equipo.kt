package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class Equipo(
    val id: Int? = null,
    val codigo: String? = null,
    val tipo: String? = null,
    val nombre: String? = null,
    val marca: String? = null,
    val modelo: String? = null,
    val serie: String? = null,
    val ubicacion: String? = null,
    val estado: String? = null,
    val fecha_adquisicion: String? = null,
    val proximo_mantenimiento: String? = null,
    val responsable: String? = null,
    val observaciones: String? = null,
    val fecha_creacion: String? = null,
    val last_modified_by: String? = null
) 