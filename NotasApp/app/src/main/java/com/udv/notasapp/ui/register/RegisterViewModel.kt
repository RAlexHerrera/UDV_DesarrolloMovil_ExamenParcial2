package com.udv.notasapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udv.notasapp.data.remote.dto.Usuario
import com.udv.notasapp.data.repository.AuthRepository
import com.udv.notasapp.util.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerState = MutableLiveData<Resource<Usuario>>()
    val registerState: LiveData<Resource<Usuario>> = _registerState

    fun registrar(nombre: String, email: String, password: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
            _registerState.value = Resource.Error("Completa todos los campos")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = Resource.Error("Ingresa un correo válido")
            return
        }
        if (password.length < 4) {
            _registerState.value = Resource.Error("La contraseña debe tener al menos 4 caracteres")
            return
        }

        _registerState.value = Resource.Loading
        viewModelScope.launch {
            _registerState.value = authRepository.register(nombre.trim(), email.trim(), password)
        }
    }
}
