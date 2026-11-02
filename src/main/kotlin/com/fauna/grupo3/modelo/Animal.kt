package com.fauna.grupo3.modelo

/**
 * - Autor: Pau Pasalodos
 * - Entidad: Animales
 * - Grupo: 3
 */
data class Animal(
    val id_animal: Int,
    val nombre: String,
    val origen: String,
    val esperanza_vida: Int,
    val peso_medio: Double
)
