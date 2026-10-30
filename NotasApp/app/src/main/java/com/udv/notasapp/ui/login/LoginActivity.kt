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
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { LoginViewModel(app.authRepository, app.sessionManager) }
        )[LoginViewModel::class.java]

        // "Recordar login": si ya hay un token guardado, saltamos directo al Dashboard.
        if (viewModel.haySesionActiva()) {
            irADashboard()
            return
        }

        binding.btnLogin.setOnClickListener {
            val usuario = binding.etUsuario.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(usuario, password)
        }

        binding.tvCrearCuenta.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.loginState.observe(this) { estado ->
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
    }

    private fun irADashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
