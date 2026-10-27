package com.udv.notasapp.data.repository

import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.ApiService
import com.udv.notasapp.data.remote.dto.LoginRequest
import com.udv.notasapp.data.remote.dto.Usuario
import com.udv.notasapp.util.Resource
import java.io.IOException

class AuthRepository(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    // Autentica contra el backend y, si todo sale bien, guarda el token de sesión.
    suspend fun login(usuario: String, password: String): Resource<Usuario> {
        return try {
            val response = apiService.login(LoginRequest(usuario, password))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                sessionManager.guardarSesion(body.token, body.usuario.nombre, body.usuario.email)
                Resource.Success(body.usuario)
            } else if (response.code() == 401) {
                Resource.Error("Usuario o contraseña incorrectos")
            } else {
                Resource.Error("Error del servidor (código ${response.code()})")
            }
        } catch (e: IOException) {
            Resource.Error("No se pudo conectar con el servidor. Verifica tu conexión")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }
}
