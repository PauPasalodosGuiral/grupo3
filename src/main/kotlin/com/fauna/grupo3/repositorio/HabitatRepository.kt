package com.fauna.grupo3.repositorio

import com.fauna.grupo3.modelo.Habitat
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.springframework.stereotype.Repository
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Autor: Jonathan Rosa
 * Entidad: Habitats
 * Grupo:3
 */
@Repository
class HabitatRepository {

    private val ruta: Path = Path.of("src/main/resources/data/habitats.csv")

    private val habitats: MutableList<Habitat> = cargarCSV().toMutableList()

    private fun cargarCSV(): List<Habitat> {
        val lista = mutableListOf<Habitat>()

        if (!Files.isReadable(ruta)) {
            println("Aviso: No se pudo leer el fichero en la ruta: $ruta")
            return lista
        }

        try {
            val reader = csvReader { delimiter = ';' }
            val filas: List<Map<String, String>> = reader.readAllWithHeader(ruta.toFile())

            for (fila in filas) {
                try {
                    val idHabitat = fila["id_habitat"]?.trim()?.toInt()!!
                    val nombre = fila["nombre"]?.trim()!!
                    val clima = fila["clima"]?.trim()!!
                    val altitudMedia = fila["altitud_media"]?.trim()?.toInt()!!
                    val temperaturaMedia = fila["temperatura_media"]?.trim()?.replace(',', '.')?.toDouble()!!

                    lista.add(Habitat(idHabitat, nombre, clima, altitudMedia, temperaturaMedia))
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
            filasParaEscribir.add(listOf("id_habitat", "nombre", "clima", "altitudMedia", "temperaturaMedia"))

            // Datos
            for (habitat in habitats) {
                filasParaEscribir.add(
                    listOf(
                        habitat.id_habitat.toString(),
                        habitat.nombre,
                        habitat.clima,
                        habitat.altitud_media.toString(),
                        habitat.temperatura_media.toString()
                    )
                )
            }

            csvWriter { delimiter = ';' }.writeAll(filasParaEscribir, fichero)
        } catch (e: Exception) {
            println("Error al guardar en el archivo CSV: ${e.message}")
        }
    }


    fun leerTodosHabitats(): List<Habitat> = habitats
    fun buscarPorID(id: Int): Habitat? = habitats.find { it.id_habitat == id }

    fun guardarHabitat(habitat: Habitat): Habitat {
        val habitatAGuardar: Habitat
        val index = habitats.indexOfFirst { it.id_habitat == habitat.id_habitat }

        if (index != -1) {
            habitats[index] = habitat
            habitatAGuardar = habitat
        } else {
            val nuevoId = if (habitat.id_habitat > 0 && buscarPorID(habitat.id_habitat) == null) {
                habitat.id_habitat
            } else {
                (habitats.maxOfOrNull { it.id_habitat } ?: 0) + 1
            }
            habitatAGuardar = habitat.copy(id_habitat = nuevoId)
            habitats.add(habitatAGuardar)
        }

        guardarEnCSV()

        return habitatAGuardar
    }
    fun borrarPorId(idHabitat: Int) {
        val eliminado = habitats.removeAll { it.id_habitat == idHabitat }
        if (eliminado) {
            guardarEnCSV()
        }
    }
}