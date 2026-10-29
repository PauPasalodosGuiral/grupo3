package com.fauna.grupo3.controlador

import org.springframework.ui.Model
import com.fauna.grupo3.modelo.Animal
import com.fauna.grupo3.servicio.AnimalService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.*

@Controller
class AnimalController(private val animalService: AnimalService) {

    // 1. LISTAR
    @GetMapping("/animales")
    fun listarAnimales(model: Model): String {
        model.addAttribute("animales", animalService.listar())
        return "animales"
    }

    // 2. DETALLE
    @GetMapping("/animal/{id_animal}")
    fun verAnimal(@PathVariable id_animal: Int, model: Model): String {
        val animal = animalService.buscar(id_animal) ?: return "errorAnimal"
        model.addAttribute("animal", animal)
        return "detalleAnimal"
    }

    // 3. FORMULARIO NUEVO ANIMAL (pasa un objeto vacío con ID 0)
    @GetMapping("/animales/nuevo")
    fun nuevoAnimal(model: Model): String {
        val animalVacio = Animal(0, "", "", 0, 0.0)
        model.addAttribute("animal", animalVacio)
        model.addAttribute("titulo", "Nuevo Animal")
        return "formAnimal"
    }

    // 4. FORMULARIO EDITAR ANIMAL (pasa el objeto existente)
    @GetMapping("/animales/editar/{id_animal}")
    fun editarAnimal(@PathVariable id_animal: Int, model: Model): String {
        val animal = animalService.buscar(id_animal) ?: return "redirect:/animales"
        model.addAttribute("animal", animal)
        model.addAttribute("titulo", "Editar Animal")
        return "formAnimal"
    }

    // 5. PROCESAR GUARDADO (Sirve tanto para crear como para editar)
    @PostMapping("/animales/guardar")
    fun guardarAnimal(@ModelAttribute animal: Animal): String {
        animalService.guardar(animal)
        return "redirect:/animales"
    }

    // 6. PROCESAR BORRADO
    @GetMapping("/animales/borrar/{id_animal}")
    fun borrarAnimal(@PathVariable id_animal: Int): String {
        animalService.borrar(id_animal)
        return "redirect:/animales"
    }
}