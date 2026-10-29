package com.fauna.grupo3.controlador

import com.fauna.grupo3.modelo.Cuidador
import com.fauna.grupo3.servicio.CuidadorService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Controller
import kotlin.system.exitProcess


@Controller
class CuidadorController(private val service: CuidadorService) : CommandLineRunner {

    override fun run(vararg args: String) {
        try {
            menuCSV()
        } catch (e: Exception) {
            return
        }
        exitProcess(0)
    }

    fun menuCSV() {
        var opcion: Int
        do {
            println("\n--------------------------------------")
            println("-------------- CRUD CSV --------------")
            println("--------------------------------------")
            println("1. Leer datos desde CSV")
            println("2. Añadir un registro nuevo al final del fichero")
            println("3. Modificar un registro existente (por ID)")
            println("4. Eliminar un registro existente (por ID)")
            println("0. Salir")
            print("Elige una opción: ")

            opcion = readln().trim().toIntOrNull() ?: -1

            when (opcion) {
                1 -> leerCSV()
                2 -> anadirRegistroCSV()
                3 -> modificarRegistroCSV()
                4 -> eliminarRegistroCSV()
                0 -> println("\n¡Hasta pronto!")
                else -> println("Opción no válida.")
            }
        } while (opcion != 0)
    }

    fun leerCSV() {
        println("\n--- LEER DATOS DESDE CSV ---")
        val lista = service.listar()
        if (lista.isEmpty()) {
            println("No hay registros que mostrar.")
            return
        }

        for (c in lista) {
            println(" - ID: ${c.id} | Nombre: ${c.nombre} | Especialidad: ${c.especialidad} | Exp: ${c.experiencia} años | Salario: ${c.tarifa}€/mes")
        }
    }

    fun anadirRegistroCSV() {
        println("\n--- AÑADIR NUEVO REGISTRO ---")

        var id: Int
        while (true) {
            print("ID (número entero): ")
            id = readln().trim().toIntOrNull() ?: -1
            if (id > 0 && !service.existeId(id)) break
            println("ID inválido o ya existente. Prueba con otro.")
        }

        print("Nombre: ")
        val nombre = readln().trim()

        print("Especialidad: ")
        val especialidad = readln().trim()

        var experiencia: Int
        while (true) {
            print("Años de experiencia (número entero): ")
            experiencia = readln().trim().toIntOrNull() ?: -1
            if (experiencia >= 0) break
            println("Introduce un número entero válido.")
        }

        var tarifa: Double
        while (true) {
            print("Salario mensual (decimal):")
            tarifa = readln().trim().toDoubleOrNull() ?: -1.0
            if (tarifa >= 0.0) break
            println("Introduce un número decimal válido.")
        }

        service.anadir(Cuidador(id, nombre, especialidad, experiencia, tarifa))
        println("Cuidador añadido correctamente.")
    }

    fun modificarRegistroCSV() {
        println("\n--- MODIFICAR REGISTRO ---")

        if (service.listar().isEmpty()) {
            println("No hay registros para modificar.")
            return
        }

        val id = pedirEntero("Introduce el ID del cuidador a modificar: ", 1)
        val cuidador = service.buscar(id)

        if (cuidador == null) {
            println("No se encontró ningún cuidador con ese ID.")
            return
        }

        println("Encontrado: ${cuidador.nombre}")

        print("Nueva especialidad: ")
        val nuevaEsp = readln().trim()

        var nuevaExp: Int
        while (true) {
            print("Nuevos años de experiencia: ")
            nuevaExp = readln().trim().toIntOrNull() ?: -1
            if (nuevaExp >= 0) break
        }

        var nuevaTarifa: Double
        while (true) {
            print("Nuevo salario mensual:")
            nuevaTarifa = readln().trim().toDoubleOrNull() ?: -1.0
            if (nuevaTarifa >= 0.0) break
        }

        service.modificar(id, nuevaEsp, nuevaExp, nuevaTarifa)
        println("Modificado correctamente.")
    }

    fun eliminarRegistroCSV() {
        println("\n--- ELIMINAR REGISTRO ---")

        if (service.listar().isEmpty()) {
            println("No hay registros.")
            return
        }

        val id = pedirEntero("Introduce el ID del cuidador a eliminar: ", 1)
        val cuidador = service.buscar(id)

        if (cuidador == null) {
            println("No se encontró ningún registro con ese ID.")
            return
        }

        print("¿Eliminar a ${cuidador.nombre}? (S/N): ")
        if (readln().trim().uppercase() == "S") {
            service.eliminar(id)
            println("Registro eliminado.")
        } else {
            println("Operación cancelada.")
        }
    }


    private fun pedirEntero(mensaje: String, minimo: Int): Int {
        while (true) {
            print(mensaje)
            val numero = readln().trim().toIntOrNull()
            if (numero != null && numero >= minimo) return numero
            println("Introduce un número entero válido (mínimo $minimo).")
        }
    }
}