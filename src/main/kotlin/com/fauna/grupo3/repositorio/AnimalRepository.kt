package com.fauna.grupo3.repositorio

import com.fauna.grupo3.modelo.Animal
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.nio.file.Files
import java.nio.file.Path

class AnimalRepository {
    fun leerCSV(ruta: Path): List<Animal> {
        var animales: List<Animal> = emptyList()
        if (!Files.isReadable(ruta)) {
            println("Error No se puede leer el fichero en la ruta: $ruta")
        } else {
            val reader = csvReader { delimiter = ';' }

            val filas: List<Map<String, String>> = reader.readAllWithHeader(ruta.toFile())

            animales = filas.mapNotNull { columnas ->
                try {
                    val id_animal = columnas["id_animal"]?.trim()?.toInt()!!
                    val nombre = columnas["nombre"]?.trim()!!
                    val origen = columnas["origen"]?.trim()!!
                    val esperanza_vida = columnas["esperanza_vida"]?.trim()?.toInt()!!
                    val peso_medio = columnas["peso_medio"]?.trim()?.toDouble()!!
                    Animal(id_animal, nombre, origen, esperanza_vida, peso_medio)
                } catch (e: Exception) {
                    println("Fila inválida ignorada: $columnas ")
                    null
                }
            }
            println("--- Información leída con éxito de: $ruta")
            for (animal in animales) {
                println(" - ID: ${animal.id_animal}, " +
                        "Nombre: ${animal.nombre}, " +
                        "Origen: ${animal.origen}, " +
                        "Esperanza de vida: ${animal.esperanza_vida} " +
                        "años, " +
                        "Peso medio: ${animal.peso_medio} kg")
            }
        }
        return animales
    }
    fun anadirCSV(ruta: Path) {
        val animales = leerCSV(ruta).toMutableList()
        var bandera: Boolean = true

        while (bandera) {
            try {
                println("Dame una id válida")
                val id: Int = readln().toInt()

                if (animales.any { it.id_animal == id }) {
                    println("El ID ya existe en el fichero. Intenta con otro.")
                } else {
                    println("Dame el nombre:")
                    val nombre = readln()

                    println("Dame el origen:")
                    val origen = readln()

                    println("Dame la esperanza de vida (número entero):")
                    val esperanza_vida = readln().toInt()

                    println("Dame el peso medio (número decimal):")
                    val peso_medio = readln().toDouble()

                    val nuevoAnimal = Animal(id, nombre, origen, esperanza_vida, peso_medio)
                    animales.add(nuevoAnimal)

                    val fichero: File = ruta.toFile()
                    csvWriter {
                        delimiter = ';'
                    }.writeAll(

                        listOf(listOf("id_animal", "nombre", "origen", "esperanza_vida", "peso_medio")) +
                                animales.map { animal ->
                                    listOf(
                                        animal.id_animal.toString(),
                                        animal.nombre,
                                        animal.origen,
                                        animal.esperanza_vida.toString(),
                                        animal.peso_medio.toString()
                                    )
                                },
                        fichero
                    )
                    println("--- Información guardada con éxito en: $fichero")
                    bandera = false
                }
            } catch (e: NumberFormatException) {
                println("Error Uno de los datos numéricos introducidos no es válido.")
            } catch (e: Exception) {
                println("Error al escribir el fichero CSV: ${e.message}")
                bandera = false
            }
        }
    }
    fun modificarCSV (ruta: Path) {
        val animales = leerCSV(ruta).toMutableList()
        var bandera: Boolean = true
        while (bandera) {
            try {
                println("Introduce el ID del registro a modificar:")
                val id: Int = readln().toInt()

                val animalEncontrado = animales.find { it.id_animal == id }

                if (animalEncontrado == null) {
                    println("No se ha encontrado ningún registro con el ID: $id")
                    bandera = false
                } else {
                    println("Registro encontrado: ${animalEncontrado.nombre}")

                    println("Dame el nuevo nombre:")
                    val nuevoNombre = readln()

                    println("Dame el nuevo origen:")
                    val nuevoOrigen = readln()

                    println("Dame la nueva esperanza de vida:")
                    val nuevaEsperanza = readln().toInt()

                    println("Dame el nuevo peso medio:")
                    val nuevoPeso = readln().replace(',', '.').toDouble()

                    val index = animales.indexOf(animalEncontrado)
                    animales[index] = Animal(id, nuevoNombre, nuevoOrigen, nuevaEsperanza, nuevoPeso)

                    val fichero: File = ruta.toFile()
                    csvWriter {
                        delimiter = ';'
                    }.writeAll(
                        listOf(listOf("id_animal", "nombre", "origen", "esperanza_vida", "peso_medio")) +
                                animales.map { animal ->
                                    listOf(
                                        animal.id_animal.toString(),
                                        animal.nombre,
                                        animal.origen,
                                        animal.esperanza_vida.toString(),
                                        animal.peso_medio.toString()
                                    )
                                },
                        fichero
                    )
                    println("--- Registro modificado e información guardada con éxito.")
                    bandera = false
                }
            } catch (e: NumberFormatException) {
                println("Error Formato numérico incorrecto.")
            } catch (e: Exception) {
                println("Error al modificar el fichero: ${e.message}")
                bandera = false
            }
        }
    }
    fun eliminarCSV(ruta: Path) {
        val animales = leerCSV(ruta).toMutableList()
        if (animales.isEmpty()) return

        var bandera: Boolean = true
        while (bandera) {
            try {
                println("Introduce el ID del registro a eliminar:")
                val id: Int = readln().toInt()

                val animalEncontrado = animales.find { it.id_animal == id }

                if (animalEncontrado == null) {
                    println("No se ha encontrado ningún registro con el ID: $id")
                    bandera = false
                } else {
                    println("Registro encontrado: ${animalEncontrado.nombre}")
                    println("¿Estás seguro de que deseas eliminar este registro? (s/n):")
                    val respuesta = readln().trim().lowercase()

                    if (respuesta == "s" || respuesta == "si" || respuesta == "sí") {
                        animales.remove(animalEncontrado)

                        val fichero: File = ruta.toFile()
                        csvWriter {
                            delimiter = ';'
                        }.writeAll(
                            listOf(listOf("id_animal", "nombre", "origen", "esperanza_vida", "peso_medio")) +
                                    animales.map { animal ->
                                        listOf(
                                            animal.id_animal.toString(),
                                            animal.nombre,
                                            animal.origen,
                                            animal.esperanza_vida.toString(),
                                            animal.peso_medio.toString()
                                        )
                                    },
                            fichero
                        )
                        println("--- Registro eliminado correctamente.")
                    } else {
                        println("Operación cancelada. No se ha eliminado ningún registro.")
                    }
                    bandera = false
                }
            } catch (e: NumberFormatException) {
                println("Error Formato numérico incorrecto.")
            } catch (e: Exception) {
                println("Error al eliminar el registro: ${e.message}")
                bandera = false
            }
        }
    }
}