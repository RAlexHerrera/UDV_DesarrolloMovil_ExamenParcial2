package com.udv.notasapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.udv.notasapp.NotasApp
import com.udv.notasapp.databinding.ActivityLoginBinding
import com.udv.notasapp.ui.dashboard.DashboardActivity
import com.udv.notasapp.ui.register.RegisterActivity
import com.udv.notasapp.util.DeviceNetworkInfo
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var app: NotasApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { LoginViewModel(app.authRepository, app.sessionManager) }
        )[LoginViewModel::class.java]

        // "Recordar login": si ya hay un token guardado, saltamos directo al Dashboard
        // sin siquiera verificar la conexión (esa pantalla hará sus propias llamadas).
        if (viewModel.haySesionActiva()) {
            irADashboard()
            return
        }

        configurarListenersLogin()
        configurarListenersDiagnostico()

        viewModel.conexionState.observe(this) { estado -> actualizarUiConexion(estado) }
        viewModel.loginState.observe(this) { estado -> actualizarUiLogin(estado) }

        viewModel.verificarConexion()
    }

    private fun configurarListenersLogin() {
        binding.btnLogin.setOnClickListener {
            val usuario = binding.etUsuario.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(usuario, password)
        }

        binding.tvCrearCuenta.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun configurarListenersDiagnostico() {
        binding.btnDiagReintentar.setOnClickListener {
            viewModel.verificarConexion()
        }

        binding.btnDiagGuardarReintentar.setOnClickListener {
            val nuevaUrl = binding.etDiagUrl.text.toString().trim()
            if (nuevaUrl.isNotBlank()) {
                app.reconfigurarBackend(nuevaUrl)
                viewModel.verificarConexion()
            }
        }
    }

    private fun actualizarUiConexion(estado: Resource<Unit>) {
        when (estado) {
            is Resource.Loading -> {
                binding.progressVerificando.visibility = View.VISIBLE
                binding.scrollDiagnostico.visibility = View.GONE
                binding.groupLoginForm.visibility = View.GONE
            }
            is Resource.Success -> {
                binding.progressVerificando.visibility = View.GONE
                binding.scrollDiagnostico.visibility = View.GONE
                binding.groupLoginForm.visibility = View.VISIBLE
            }
            is Resource.Error -> {
                binding.progressVerificando.visibility = View.GONE
                binding.groupLoginForm.visibility = View.GONE
                binding.scrollDiagnostico.visibility = View.VISIBLE
                mostrarDiagnostico(estado.message)
            }
        }
    }

    private fun mostrarDiagnostico(mensaje: String) {
        binding.tvDiagMensaje.text = mensaje
        binding.etDiagUrl.setText(app.apiServiceProvider.baseUrl)

        val ips = DeviceNetworkInfo.obtenerIpsLocales()
        binding.tvDiagIps.text = if (ips.isEmpty()) "No se pudo determinar (sin red activa)" else ips.joinToString(", ")
    }

    private fun actualizarUiLogin(estado: Resource<com.udv.notasapp.data.remote.dto.Usuario>) {
        when (estado) {
            is Resource.Loading -> {
                binding.progressLogin.visibility = View.VISIBLE
                binding.btnLogin.isEnabled = false
                binding.tvError.visibility = View.GONE
            }
            is Resource.Success -> {
                binding.progressLogin.visibility = View.GONE
                irADashboard()
            }
            is Resource.Error -> {
                binding.progressLogin.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.tvError.text = estado.message
                binding.tvError.visibility = View.VISIBLE
            }
        }
    }

    private fun irADashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
