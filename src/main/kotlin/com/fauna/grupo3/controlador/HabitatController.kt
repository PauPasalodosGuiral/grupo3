package com.fauna.grupo3.controlador

import com.fauna.grupo3.modelo.Habitat
import com.fauna.grupo3.servicio.HabitatService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * Autor: Jonathan Rosa (adaptado)
 * Entidad: Habitat
 * Grupo: 3
 */
@Controller
class HabitatController(private val habitatService: HabitatService) {

    // 1. LISTAR HÁBITATS
    @GetMapping("/habitats")
    fun listarHabitats(model: Model): String {
        model.addAttribute("habitats", habitatService.listar())
        return "habitats" // Nombre de la vista HTML (habitats.html)
    }

    // 2. DETALLE DE UN HÁBITAT
    @GetMapping("/habitat/{id_habitat}")
    fun verHabitat(@PathVariable id_habitat: Int, model: Model): String {
        val habitat = habitatService.buscar(id_habitat) ?: return "errorHabitat"
        model.addAttribute("habitat", habitat)
        return "detalleHabitat" // Nombre de la vista HTML de detalle
    }

    // 3. FORMULARIO NUEVO HÁBITAT (pasa un objeto vacío con ID 0 y valores por defecto)
    @GetMapping("/habitats/nuevo")
    fun nuevoHabitat(model: Model): String {
        val habitatVacio = Habitat(
            id_habitat = 0,
            nombre = "",
            clima = "",
            altitud_media = 0,
            temperatura_media = 0.0
        )
        model.addAttribute("habitat", habitatVacio)
        model.addAttribute("titulo", "Nuevo Hábitat")
        return "formHabitat" // Nombre de la vista HTML del formulario
    }

    // 4. FORMULARIO EDITAR HÁBITAT (pasa el objeto existente)
    @GetMapping("/habitats/editar/{id_habitat}")
    fun editarHabitat(@PathVariable id_habitat: Int, model: Model): String {
        val habitat = habitatService.buscar(id_habitat) ?: return "redirect:/habitats"
        model.addAttribute("habitat", habitat)
        model.addAttribute("titulo", "Editar Hábitat")
        return "formHabitat" // Reutiliza la misma vista de formulario
    }

    // 5. PROCESAR GUARDADO (Sirve tanto para crear como para editar)
    @PostMapping("/habitats/guardar")
    fun guardarHabitat(@ModelAttribute habitat: Habitat): String {
        habitatService.guardar(habitat)
        return "redirect:/habitats"
    }

    // 6. PROCESAR BORRADO
    @GetMapping("/habitats/borrar/{id_habitat}")
    fun borrarHabitat(@PathVariable id_habitat: Int): String {
        habitatService.borrar(id_habitat)
        return "redirect:/habitats"
    }
}