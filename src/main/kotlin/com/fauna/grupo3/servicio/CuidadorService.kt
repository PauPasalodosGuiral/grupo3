package com.fauna.grupo3.servicio

import com.fauna.grupo3.modelo.Cuidador
import com.fauna.grupo3.repositorio.CuidadorRepository
import org.springframework.stereotype.Service

@Service
class CuidadorService(private val repository: CuidadorRepository) {

    fun listar(): List<Cuidador> = repository.leerListaCSV()

    fun buscar(id: Int): Cuidador? = listar().find { it.id == id }

    fun existeId(id: Int): Boolean = buscar(id) != null

    fun anadir(cuidador: Cuidador) {
        val lista = listar().toMutableList()
        lista.add(cuidador)
        repository.guardarCSV(lista)
    }

    fun modificar(id: Int, especialidad: String, experiencia: Int, tarifa: Double): Boolean {
        val lista = listar().toMutableList()
        val indice = lista.indexOfFirst { it.id == id }
        if (indice == -1) return false

        lista[indice] = lista[indice].copy(especialidad = especialidad, experiencia = experiencia, tarifa = tarifa)
        repository.guardarCSV(lista)
        return true
    }

    fun eliminar(id: Int): Boolean {
        val lista = listar().toMutableList()
        val cuidador = lista.find { it.id == id } ?: return false

        lista.remove(cuidador)
        repository.guardarCSV(lista)
        return true
    }
}