package com.udv.notasapp.data.remote.dto

// POST api/auth/register — crea la cuenta y responde igual que login (token + usuario),
// por eso reutiliza LoginResponse como tipo de respuesta.
data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)
