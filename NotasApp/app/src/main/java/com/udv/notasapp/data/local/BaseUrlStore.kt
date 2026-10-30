package com.udv.notasapp.data.local

import android.content.Context

/**
 * Guarda la URL base del backend en SharedPreferences normales (no es información sensible,
 * a diferencia del token). Permite cambiarla desde el panel de diagnóstico de conexión sin
 * tener que recompilar la app — útil cuando se prueba en emulador vs. dispositivo físico.
 */
class BaseUrlStore(context: Context) {

    private val prefs = context.getSharedPreferences("notas_app_config", Context.MODE_PRIVATE)

    fun obtenerBaseUrl(): String = prefs.getString(KEY_BASE_URL, null) ?: DEFAULT_BASE_URL

    fun guardarBaseUrl(url: String) {
        prefs.edit().putString(KEY_BASE_URL, url).apply()
    }

    companion object {
        private const val KEY_BASE_URL = "base_url"

        // Alias especial del emulador de Android Studio: apunta al localhost de la PC anfitriona.
        const val DEFAULT_BASE_URL = "http://10.0.2.2:3000/"
    }
}
