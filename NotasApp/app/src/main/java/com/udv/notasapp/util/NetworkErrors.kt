package com.udv.notasapp.util

import com.google.gson.Gson
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

private data class ErrorBody(val mensaje: String?)

/**
 * El backend manda {"mensaje": "..."} en sus respuestas de error (400/401/409/...).
 * Esto lo lee para mostrar ese mensaje específico en vez de uno genérico con el código HTTP.
 */
fun Response<*>.mensajeDeErrorBackend(mensajePorDefecto: String): String {
    val crudo = errorBody()?.string()
    if (crudo.isNullOrBlank()) return mensajePorDefecto
    return try {
        Gson().fromJson(crudo, ErrorBody::class.java)?.mensaje ?: mensajePorDefecto
    } catch (e: Exception) {
        mensajePorDefecto
    }
}

/**
 * Traduce una excepción de red a un mensaje específico en español, en vez del genérico
 * "no se pudo conectar" para todo. Se usa en todos los Repository para que la UI pueda
 * mostrarle al usuario (y a nosotros, depurando) la causa real.
 */
fun mensajeDeErrorRed(e: Exception): String {
    return when (e) {
        is UnknownHostException ->
            "No se pudo resolver la dirección del servidor (host desconocido). Revisa la URL configurada."
        is ConnectException ->
            "Conexión rechazada: el servidor no está escuchando en esa IP/puerto, o está bloqueado por el firewall."
        is SocketTimeoutException ->
            "Tiempo de espera agotado intentando conectar. El servidor no respondió a tiempo (posible problema de red)."
        is SSLHandshakeException ->
            "Error de certificado/TLS al conectar."
        else ->
            "Error de red inesperado: ${e::class.simpleName ?: "desconocido"} — ${e.message ?: "sin detalle"}"
    }
}
