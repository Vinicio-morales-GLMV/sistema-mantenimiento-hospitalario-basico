package com.example.myapplication

import android.util.Log
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.PostgrestRequestBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EquipoService {
    private val supabase = SupabaseConfig.client

    suspend fun obtenerEquipos(): List<Equipo> = withContext(Dispatchers.IO) {
        try {
            val response = supabase.postgrest["equipos"].select()
            Log.d("SupabaseResponse", "Raw response: ${'$'}response")
            val equipos = response.decodeList<Equipo>()
            Log.d("SupabaseResponse", "Equipos decodificados: ${'$'}equipos")
            equipos
        } catch (e: Exception) {
            Log.e("SupabaseError", "Error al obtener equipos", e)
            throw e
        }
    }

    suspend fun obtenerEquipoPorId(id: Int): Equipo? = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["equipos"].select {
                filter {
                    eq("id", id)
                }
            }.decodeSingleOrNull<Equipo>()
        } catch (e: Exception) {
            Log.e("SupabaseError", "Error al obtener equipo por id", e)
            null
        }
    }

    suspend fun actualizarEstadoEquipo(id: Int, nuevoEstado: String): Boolean = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["equipos"].update({
                set("estado", nuevoEstado)
            }) {
                filter {
                    eq("id", id)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("SupabaseError", "Error al actualizar estado de equipo", e)
            false
        }
    }
} 