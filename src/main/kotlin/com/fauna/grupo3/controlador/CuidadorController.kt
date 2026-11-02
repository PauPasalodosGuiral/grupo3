/**
 * - Autor: Izan Moreno
 * - Entidad: Cuidadores
 * - Grupo: 3
 */
package com.fauna.grupo3.controlador

import com.fauna.grupo3.modelo.Cuidador
import com.fauna.grupo3.servicio.CuidadorService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class CuidadorController(private val service: CuidadorService) {

    @GetMapping("/cuidadores")
    fun listarCuidadores(model: Model): String {
        model.addAttribute("cuidadores", service.listar())
        return "cuidadores"
    }

    @GetMapping("/cuidador/{id}")
    fun verCuidador(@PathVariable id: Int, model: Model): String {
        val cuidador = service.buscar(id) ?: return "errorCuidador"
        model.addAttribute("cuidador", cuidador)
        return "detalleCuidador"
    }

    @GetMapping("/cuidadores/nuevo")
    fun mostrarFormularioAnadir(model: Model): String {
        model.addAttribute("cuidador", Cuidador(0, "", "", 0, 0.0))
        model.addAttribute("titulo", "Nuevo cuidador")
        return "formCuidador"
    }

    @GetMapping("/cuidadores/editar/{id}")
    fun mostrarFormularioModificar(@PathVariable id: Int, model: Model): String {
        val cuidador = service.buscar(id) ?: return "redirect:/cuidadores"
        model.addAttribute("cuidador", cuidador)
        model.addAttribute("titulo", "Editar cuidador")
        return "formCuidador"
    }

    @PostMapping("/cuidadores/guardar")
    fun guardarCuidador(@ModelAttribute cuidador: Cuidador): String {
        service.guardar(cuidador)
        return "redirect:/cuidadores"
    }


    @GetMapping("/cuidadores/borrar/{id}")
    fun borrarCuidador(@PathVariable id: Int): String {
        service.eliminar(id)
        return "redirect:/cuidadores"
    }
}