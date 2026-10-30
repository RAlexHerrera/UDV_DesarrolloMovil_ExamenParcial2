package com.udv.notasapp.data.repository

import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.ApiServiceProvider
import com.udv.notasapp.data.remote.dto.Nota
import com.udv.notasapp.data.remote.dto.NotaRequest
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.mensajeDeErrorBackend
import com.udv.notasapp.util.mensajeDeErrorRed
import java.io.IOException

class NotasRepository(
    private val apiServiceProvider: ApiServiceProvider,
    private val sessionManager: SessionManager
) {

    // Si el backend responde 401, el token ya no es válido: se cierra la sesión local
    // para que la siguiente pantalla mande al usuario de vuelta al login.
    private fun manejarSesionExpirada() = sessionManager.cerrarSesion()

    suspend fun crearNota(titulo: String, contenido: String): Resource<Nota> {
        return try {
            val response = apiServiceProvider.apiService.crearNota(NotaRequest(titulo, contenido))
            val body = response.body()

            when {
                response.isSuccessful && body != null -> Resource.Success(body)
                response.code() == 401 -> {
                    manejarSesionExpirada()
                    Resource.Error("Tu sesión expiró, inicia sesión de nuevo")
                }
                else -> Resource.Error(response.mensajeDeErrorBackend("No se pudo guardar la nota (código ${response.code()})"))
            }
        } catch (e: IOException) {
            Resource.Error(mensajeDeErrorRed(e))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }

    suspend fun obtenerNotas(): Resource<List<Nota>> {
        return try {
            val response = apiServiceProvider.apiService.obtenerNotas()

            when {
                response.isSuccessful -> Resource.Success(response.body() ?: emptyList())
                response.code() == 401 -> {
                    manejarSesionExpirada()
                    Resource.Error("Tu sesión expiró, inicia sesión de nuevo")
                }
                else -> Resource.Error(response.mensajeDeErrorBackend("No se pudieron cargar las notas (código ${response.code()})"))
            }
        } catch (e: IOException) {
            Resource.Error(mensajeDeErrorRed(e))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Ocurrió un error inesperado")
        }
    }
}
