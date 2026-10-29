/**
 * - Autor: Izan Moreno
 * - Entidad: Cuidadores
 * - Grupo: 3
 */
package com.fauna.grupo3.repositorio

import com.fauna.grupo3.modelo.Cuidador
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import org.springframework.stereotype.Repository
import java.nio.file.Files
import java.nio.file.Path

@Repository
class CuidadorRepository {

    val RUTA_CSV: Path = Path.of("src", "main", "resources", "data", "cuidadores.csv")

    fun leerListaCSV(): List<Cuidador> {
        if (!Files.isReadable(RUTA_CSV)) {
            return emptyList()
        }

        val filas: List<List<String>> = csvReader { delimiter = ';' }.readAll(RUTA_CSV.toFile())

        return filas.mapNotNull { columnas ->
            if (columnas.size >= 5) {
                try {
                    Cuidador(columnas[0].toInt(), columnas[1], columnas[2], columnas[3].toInt(), columnas[4].toDouble())
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    fun guardarCSV(lista: List<Cuidador>, ruta: Path = RUTA_CSV) {
        Files.createDirectories(ruta.parent)
        csvWriter { delimiter = ';' }.writeAll(
            lista.map {
                listOf(
                    it.id.toString(),
                    it.nombre,
                    it.especialidad,
                    it.experiencia.toString(),
                    it.tarifa.toString()
                )
            },
            ruta.toFile()
        )
    }
}