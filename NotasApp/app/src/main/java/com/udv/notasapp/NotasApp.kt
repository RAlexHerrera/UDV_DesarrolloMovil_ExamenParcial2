package com.udv.notasapp

import android.app.Application
import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.ApiService
import com.udv.notasapp.data.remote.RetrofitClient
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
    private lateinit var apiService: ApiService

    lateinit var authRepository: AuthRepository
        private set
    lateinit var notasRepository: NotasRepository
        private set

    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager(this)
        apiService = RetrofitClient.create(sessionManager)
        authRepository = AuthRepository(apiService, sessionManager)
        notasRepository = NotasRepository(apiService, sessionManager)
    }
}
