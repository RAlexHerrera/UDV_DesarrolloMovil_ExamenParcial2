package com.udv.notasapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udv.notasapp.data.local.SessionManager
import com.udv.notasapp.data.remote.dto.Usuario
import com.udv.notasapp.data.repository.AuthRepository
import com.udv.notasapp.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Usuario>>()
    val loginState: LiveData<Resource<Usuario>> = _loginState

    // Estado de la verificación de conexión al backend, previa a mostrar el formulario.
    private val _conexionState = MutableLiveData<Resource<Unit>>()
    val conexionState: LiveData<Resource<Unit>> = _conexionState

    // Para saltar el login si ya hay una sesión guardada ("recordar login").
    fun haySesionActiva(): Boolean = sessionManager.haySesionActiva()

    fun verificarConexion() {
        _conexionState.value = Resource.Loading
        viewModelScope.launch {
            _conexionState.value = authRepository.verificarConexion()
        }
    }

    fun login(usuario: String, password: String) {
        if (usuario.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Completa todos los campos")
            return
        }

        _loginState.value = Resource.Loading
        viewModelScope.launch {
            _loginState.value = authRepository.login(usuario.trim(), password)
        }
    }
}
