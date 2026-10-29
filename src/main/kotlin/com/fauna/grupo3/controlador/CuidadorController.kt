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
@RequestMapping("/cuidadores")
class CuidadorController(private val service: CuidadorService) {

    @GetMapping
    fun listarCuidadores(model: Model): String {
        model.addAttribute("cuidadores", service.listar())
        return "cuidadores/lista"
    }

    @GetMapping("/nuevo")
    fun mostrarFormularioAñadir(model: Model): String {
        model.addAttribute("cuidador", Cuidador(0, "", "", 0, 0.0))
        return "cuidadores/formulario"
    }

    @PostMapping("/guardar")
    fun guardarCuidador(@ModelAttribute cuidador: Cuidador): String {
        if (cuidador.id > 0 && !service.existeId(cuidador.id)) {
            service.anadir(cuidador)
        }
        return "redirect:/cuidadores"
    }

    @GetMapping("/editar/{id}")
    fun mostrarFormularioModificar(@PathVariable id: Int, model: Model): String {
        val cuidador = service.buscar(id) ?: return "redirect:/cuidadores"
        model.addAttribute("cuidador", cuidador)
        return "cuidadores/formulario"
    }

    @PostMapping("/actualizar/{id}")
    fun actualizarCuidador(
        @PathVariable id: Int,
        @RequestParam especialidad: String,
        @RequestParam experiencia: Int,
        @RequestParam tarifa: Double
    ): String {
        service.modificar(id, especialidad, experiencia, tarifa)
        return "redirect:/cuidadores"
    }

    @GetMapping("/eliminar/{id}")
    fun eliminarCuidador(@PathVariable id: Int): String {
        service.eliminar(id)
        return "redirect:/cuidadores"
    }
}