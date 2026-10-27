package com.udv.notasapp.data.remote.dto

// Contrato de notas, verificado contra NotasApi (ver carpeta ../NotasApi).
// POST api/notas (crear)  |  GET api/notas (listar)

data class NotaRequest(
    val titulo: String,
    val contenido: String
)

data class Nota(
    val id: Int,
    val titulo: String,
    val contenido: String,
    val fechaCreacion: String?
)
