package com.fauna.grupo3.modelo

/**
 * Autor: Jonathan Rosa
 * Entidad: Habitat
 * Grupo: 3
 */
    // Data class CSV
    data class Habitat(
        val id_habitat: Int,
        val nombre: String,
        val clima: String,
        val altitud_media: Int,
        val temperatura_media: Double
    )
