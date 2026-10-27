package com.udv.notasapp.ui.dashboard

import androidx.lifecycle.ViewModel
import com.udv.notasapp.data.local.SessionManager

class DashboardViewModel(private val sessionManager: SessionManager) : ViewModel() {

    fun obtenerNombreUsuario(): String = sessionManager.obtenerNombre() ?: "Usuario"

    fun cerrarSesion() = sessionManager.cerrarSesion()
}
