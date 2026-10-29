package com.fauna.grupo3.servicio

import com.fauna.grupo3.modelo.Habitat
import com.fauna.grupo3.repositorio.HabitatRepository

/**
 * Autor: Jonathan Rosa Aledón
 * Entidad: Habitat
 * Grupo: 3
 */
class HabitatService(private val habitatRepository: HabitatRepository) {
    fun listar(): List<Habitat> = habitatRepository.leerTodosHabitats()
    fun buscar(id: Int): Habitat? = habitatRepository.buscarPorID(id)
    fun guardar(habitat: Habitat): Habitat = habitatRepository.guardarHabitat(habitat)
    fun borrar(id: Int) {habitatRepository.borrarPorId(id)}

}