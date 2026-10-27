package com.udv.notasapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.udv.notasapp.NotasApp
import com.udv.notasapp.R
import com.udv.notasapp.databinding.ActivityDashboardBinding
import com.udv.notasapp.ui.createnote.CreateNoteActivity
import com.udv.notasapp.ui.login.LoginActivity
import com.udv.notasapp.ui.noteslist.NotesListActivity
import com.udv.notasapp.util.ViewModelFactory

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { DashboardViewModel(app.sessionManager) }
        )[DashboardViewModel::class.java]

        binding.tvBienvenida.text = getString(R.string.dashboard_welcome, viewModel.obtenerNombreUsuario())

        binding.btnCrearNota.setOnClickListener {
            startActivity(Intent(this, CreateNoteActivity::class.java))
        }

        binding.btnVerNotas.setOnClickListener {
            startActivity(Intent(this, NotesListActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            viewModel.cerrarSesion()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
