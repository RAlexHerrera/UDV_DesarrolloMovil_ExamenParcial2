package com.udv.notasapp.data.remote

import com.udv.notasapp.data.local.SessionManager

/**
 * Los Repository reciben este provider (no un ApiService fijo), para poder cambiar la
 * URL del backend en caliente desde el panel de diagnóstico de conexión: al llamar
 * actualizar(), todos los Repository que ya existían ven el ApiService nuevo de inmediato,
 * sin tener que recrear ViewModels ni Activities.
 */
class ApiServiceProvider(
    private val sessionManager: SessionManager,
    baseUrlInicial: String
) {
    var baseUrl: String = baseUrlInicial
        private set

    var apiService: ApiService = RetrofitClient.create(sessionManager, baseUrlInicial)
        private set

    fun actualizar(nuevaBaseUrl: String) {
        baseUrl = nuevaBaseUrl
        apiService = RetrofitClient.create(sessionManager, nuevaBaseUrl)
    }
}
