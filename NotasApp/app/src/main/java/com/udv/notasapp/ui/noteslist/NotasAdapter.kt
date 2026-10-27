package com.udv.notasapp.ui.noteslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udv.notasapp.data.remote.dto.Nota
import com.udv.notasapp.databinding.ItemNotaBinding

// ListAdapter + DiffUtil: solo redibuja los items que realmente cambiaron, no toda la lista.
class NotasAdapter : ListAdapter<Nota, NotasAdapter.NotaViewHolder>(NotaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = ItemNotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotaViewHolder(private val binding: ItemNotaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota) {
            binding.tvItemTitulo.text = nota.titulo
            binding.tvItemContenido.text = nota.contenido
            binding.tvItemFecha.text = nota.fechaCreacion ?: ""
        }
    }

    private class NotaDiffCallback : DiffUtil.ItemCallback<Nota>() {
        override fun areItemsTheSame(oldItem: Nota, newItem: Nota) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Nota, newItem: Nota) = oldItem == newItem
    }
}
