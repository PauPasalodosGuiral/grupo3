package com.fauna.grupo3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType

@SpringBootApplication
@RestController
class Grupo3Application {

	@GetMapping("/animal", produces = [MediaType.TEXT_HTML_VALUE])
	fun infoAnimal(
		@RequestParam(value = "nombre", defaultValue = "Lobo ibérico") nombre: String
	): String {
		return """
            <!DOCTYPE HTML>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <title>Detalle de Fauna</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
            </head>
            <body class="bg-light">
                <div class="container mt-5">

                    <div class="alert alert-primary shadow-sm" role="alert">
                        <h4 class="alert-heading">Consulta Realizada</h4>
                        <p class="mb-0">
                            Estás consultando información sobre la especie: <strong>$nombre</strong>.
                        </p>
                    </div>

                    <a href="/" class="btn btn-outline-secondary btn-sm mt-2">Volver al inicio</a>

                </div>
            </body>
            </html>
        """.trimIndent()
	}
}

fun main(args: Array<String>) {
	runApplication<Grupo3Application>(*args)
}
