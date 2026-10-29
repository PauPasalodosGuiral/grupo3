package com.fauna.grupo3.repositorio

import com.fauna.grupo3.modelo.Animal
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

@Repository
class AnimalRepository {

    private val ruta: Path = Path.of("src/main/resources/data/animales.csv")

    private val animales: MutableList<Animal> = cargarCSV().toMutableList()

    private fun cargarCSV(): List<Animal> {
        val lista = mutableListOf<Animal>()

        if (!Files.isReadable(ruta)) {
            println("Aviso: No se pudo leer el fichero en la ruta: $ruta")
            return lista
        }

        try {
            val reader = csvReader { delimiter = ';' }
            val filas: List<Map<String, String>> = reader.readAllWithHeader(ruta.toFile())

            for (fila in filas) {
                try {
                    val idAnimal = fila["id_animal"]?.trim()?.toInt()!!
                    val nombre = fila["nombre"]?.trim()!!
                    val origen = fila["origen"]?.trim()!!
                    val esperanzaVida = fila["esperanza_vida"]?.trim()?.toInt()!!
                    val pesoMedio = fila["peso_medio"]?.trim()?.replace(',', '.')?.toDouble()!!

                    lista.add(Animal(idAnimal, nombre, origen, esperanzaVida, pesoMedio))
                } catch (e: Exception) {
                    println("Fila inválida omitida: $fila")
                }
            }
        } catch (e: Exception) {
            println("Error al cargar el archivo CSV: ${e.message}")
        }

        return lista
    }

    private fun guardarEnCSV() {
        try {
            val fichero: File = ruta.toFile()
            val filasParaEscribir = mutableListOf<List<String>>()

            // Encabezados
            filasParaEscribir.add(listOf("id_animal", "nombre", "origen", "esperanza_vida", "peso_medio"))

            // Datos
            for (animal in animales) {
                filasParaEscribir.add(
                    listOf(
                        animal.id_animal.toString(),
                        animal.nombre,
                        animal.origen,
                        animal.esperanza_vida.toString(),
                        animal.peso_medio.toString()
                    )
                )
            }

            csvWriter { delimiter = ';' }.writeAll(filasParaEscribir, fichero)
        } catch (e: Exception) {
            println("Error al guardar en el archivo CSV: ${e.message}")
        }
    }


    fun leerTodosAnimales(): List<Animal> = animales
    fun buscarPorID(id: Int): Animal? = animales.find { it.id_animal == id }

    fun guardarAnimal(animal: Animal) {
        val index = animales.indexOfFirst { it.id_animal == animal.id_animal }

        if (index != -1) {
            // Edición de un animal existente
            animales[index] = animal
        } else {
            // Creación de un nuevo animal (si no trae ID o es 0, asigna autoincremental)
            val nuevoId = if (animal.id_animal > 0 && buscarPorID(animal.id_animal) == null) {
                animal.id_animal
            } else {
                (animales.maxOfOrNull { it.id_animal } ?: 0) + 1
            }
            animales.add(animal.copy(id_animal = nuevoId))
        }

        // Sincroniza los cambios con el archivo CSV
        guardarEnCSV()
    }

    fun borrarPorId(idAnimal: Int) {
        val eliminado = animales.removeAll { it.id_animal == idAnimal }
        if (eliminado) {
            guardarEnCSV()
        }
    }
}