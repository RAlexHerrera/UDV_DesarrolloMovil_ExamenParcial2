package com.udv.notasapp.data.remote.dto

// GET api/health — usado por la app para verificar la conexión antes de mostrar el login.
data class HealthResponse(
    val status: String,
    val servicio: String?
)
