package com.udv.notasapp.data.remote.dto

// Contrato del login, verificado contra NotasApi (ver carpeta ../NotasApi).
// POST api/auth/login

data class LoginRequest(
    val email: String,
    val password: String
)

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String
)

data class LoginResponse(
    val token: String,
    val usuario: Usuario
)
