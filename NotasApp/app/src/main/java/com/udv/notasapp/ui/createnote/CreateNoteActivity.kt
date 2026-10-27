package com.udv.notasapp.ui.createnote

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.udv.notasapp.NotasApp
import com.udv.notasapp.R
import com.udv.notasapp.databinding.ActivityCreateNoteBinding
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.ViewModelFactory

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var viewModel: CreateNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { CreateNoteViewModel(app.notasRepository) }
        )[CreateNoteViewModel::class.java]

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString()
            val contenido = binding.etContenido.text.toString()
            viewModel.crearNota(titulo, contenido)
        }

        viewModel.createState.observe(this) { estado ->
            when (estado) {
                is Resource.Loading -> {
                    binding.progressCreate.visibility = View.VISIBLE
                    binding.tvMensaje.visibility = View.GONE
                    binding.btnGuardar.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressCreate.visibility = View.GONE
                    binding.btnGuardar.isEnabled = true
                    mostrarMensaje(getString(R.string.msg_nota_guardada), esError = false)
                    binding.etTitulo.text.clear()
                    binding.etContenido.text.clear()
                }
                is Resource.Error -> {
                    binding.progressCreate.visibility = View.GONE
                    binding.btnGuardar.isEnabled = true
                    mostrarMensaje(estado.message, esError = true)
                }
            }
        }
    }

    private fun mostrarMensaje(texto: String, esError: Boolean) {
        binding.tvMensaje.text = texto
        binding.tvMensaje.setTextColor(
            getColor(if (esError) R.color.error else R.color.success)
        )
        binding.tvMensaje.visibility = View.VISIBLE
    }
}
