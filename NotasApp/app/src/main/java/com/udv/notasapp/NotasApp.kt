package com.udv.notasapp

import android.app.Application
import com.udv.notasapp.data.local.BaseUrlStore
import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.ApiServiceProvider
import com.udv.notasapp.data.repository.AuthRepository
import com.udv.notasapp.data.repository.NotasRepository

/**
 * Punto único donde se crean las dependencias compartidas (sesión, red, repositorios).
 * No se usa un framework de inyección de dependencias (Hilt/Koin) para mantener el
 * proyecto simple; cada Activity las obtiene desde aquí mediante (application as NotasApp).
 */
class NotasApp : Application() {

    lateinit var sessionManager: SessionManager
        private set
    lateinit var baseUrlStore: BaseUrlStore
        private set
    lateinit var apiServiceProvider: ApiServiceProvider
        private set

    lateinit var authRepository: AuthRepository
        private set
    lateinit var notasRepository: NotasRepository
        private set

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        baseUrlStore = BaseUrlStore(this)
        apiServiceProvider = ApiServiceProvider(sessionManager, baseUrlStore.obtenerBaseUrl())
        authRepository = AuthRepository(apiServiceProvider, sessionManager)
        notasRepository = NotasRepository(apiServiceProvider, sessionManager)
    }

    /** Cambia la URL del backend en caliente (panel de diagnóstico de conexión) y la recuerda. */
    fun reconfigurarBackend(nuevaBaseUrl: String) {
        apiServiceProvider.actualizar(nuevaBaseUrl)
        baseUrlStore.guardarBaseUrl(nuevaBaseUrl)
    }
}
