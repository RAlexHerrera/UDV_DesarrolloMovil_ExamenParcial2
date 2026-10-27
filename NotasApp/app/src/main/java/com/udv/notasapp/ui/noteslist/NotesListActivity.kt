package com.udv.notasapp.ui.noteslist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.udv.notasapp.NotasApp
import com.udv.notasapp.databinding.ActivityNotesListBinding
import com.udv.notasapp.util.Resource
import com.udv.notasapp.util.ViewModelFactory

class NotesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesListBinding
    private lateinit var viewModel: NotesListViewModel
    private val adapter = NotasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val app = application as NotasApp
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory { NotesListViewModel(app.notasRepository) }
        )[NotesListViewModel::class.java]

        binding.btnBack.setOnClickListener { finish() }

        binding.recyclerNotas.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotas.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { viewModel.cargarNotas() }

        viewModel.notasState.observe(this) { estado ->
            when (estado) {
                is Resource.Loading -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressList.visibility = View.VISIBLE
                    }
                    binding.tvEmpty.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressList.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    adapter.submitList(estado.data)
                    binding.tvEmpty.visibility = if (estado.data.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    binding.progressList.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this, estado.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.cargarNotas()
    }
}
