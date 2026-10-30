package com.udv.notasapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.udv.notasapp.NotasApp
import com.udv.notasapp.databinding.ActivityRegisterBinding
import com.udv.notasapp.ui.dashboard.DashboardActivity
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { RegisterViewModel(app.authRepository) }
        )[RegisterViewModel::class.java]

        binding.tvYaTengoCuenta.setOnClickListener { finish() } // vuelve al Login

        binding.btnCrearCuenta.setOnClickListener {
            viewModel.registrar(
                binding.etNombre.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        viewModel.registerState.observe(this) { estado ->
            when (estado) {
                is Resource.Loading -> {
                    binding.progressRegister.visibility = View.VISIBLE
                    binding.btnCrearCuenta.isEnabled = false
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressRegister.visibility = View.GONE
                    // Ya quedó la sesión guardada (ver AuthRepository.register) -> directo al Dashboard.
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finishAffinity()
                }
                is Resource.Error -> {
                    binding.progressRegister.visibility = View.GONE
                    binding.btnCrearCuenta.isEnabled = true
                    binding.tvError.text = estado.message
                    binding.tvError.visibility = View.VISIBLE
                }
            }
        }
    }
}
