package com.udv.notasapp.data.repository

import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.ApiServiceProvider
import com.udv.notasapp.data.remote.dto.LoginRequest
import com.udv.notasapp.data.remote.dto.RegisterRequest
import com.udv.notasapp.data.remote.dto.Usuario
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.mensajeDeErrorBackend
import com.udv.notasapp.util.mensajeDeErrorRed
import java.io.IOException

class AuthRepository(
    private val apiServiceProvider: ApiServiceProvider,
    private val sessionManager: SessionManager
) {

    // Verifica que el backend responda, antes de mostrar el formulario de login.
    suspend fun verificarConexion(): Resource<Unit> {
        return try {
            val response = apiServiceProvider.apiService.verificarConexion()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.mensajeDeErrorBackend("El servidor respondió con error (código ${response.code()})"))
            }
        } catch (e: IOException) {
            Resource.Error(mensajeDeErrorRed(e))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }

    // Autentica contra el backend y, si todo sale bien, guarda el token de sesión.
    suspend fun login(usuario: String, password: String): Resource<Usuario> {
        return try {
            val response = apiServiceProvider.apiService.login(LoginRequest(usuario, password))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                sessionManager.guardarSesion(body.token, body.usuario.nombre, body.usuario.email)
                Resource.Success(body.usuario)
            } else if (response.code() == 401) {
                Resource.Error(response.mensajeDeErrorBackend("Usuario o contraseña incorrectos"))
            } else {
                Resource.Error(response.mensajeDeErrorBackend("Error del servidor (código ${response.code()})"))
            }
        } catch (e: IOException) {
            Resource.Error(mensajeDeErrorRed(e))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }

    // Crea la cuenta y, si sale bien, ya deja la sesión guardada (el backend
    // devuelve token igual que en login, para no pedir loguearse aparte).
    suspend fun register(nombre: String, email: String, password: String): Resource<Usuario> {
        return try {
            val response = apiServiceProvider.apiService.register(RegisterRequest(nombre, email, password))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                sessionManager.guardarSesion(body.token, body.usuario.nombre, body.usuario.email)
                Resource.Success(body.usuario)
            } else if (response.code() == 409) {
                Resource.Error(response.mensajeDeErrorBackend("Ya existe una cuenta con ese correo"))
            } else {
                Resource.Error(response.mensajeDeErrorBackend("Error del servidor (código ${response.code()})"))
            }
        } catch (e: IOException) {
            Resource.Error(mensajeDeErrorRed(e))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }
}
