package com.udv.notasapp.util

/**
 * Envoltorio de estado para exponer en LiveData el resultado de una operación async:
 * cargando, éxito con datos, o error con un mensaje ya listo para mostrar al usuario.
 */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
