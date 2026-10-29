package com.fauna.grupo3.servicio

import com.fauna.grupo3.modelo.Animal
import com.fauna.grupo3.repositorio.AnimalRepository
import org.springframework.stereotype.Service

@Service
class AnimalService(private val animalRepository: AnimalRepository) {
    fun listar(): List<Animal> = animalRepository.leerTodosAnimales()
    fun buscar(id: Int): Animal? = animalRepository.buscarPorID(id)
    fun guardar(animal: Animal): Animal = animalRepository.guardarAnimal(animal)
    fun borrar(id: Int) {animalRepository.borrarPorId(id)}

}