/**
 * - Autor: Izan Moreno
 * - Entidad: Cuidadores
 * - Grupo: 3
 */
package com.fauna.grupo3.modelo

data class Cuidador(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: Int,
    val tarifa: Double
)