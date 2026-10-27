package com.udv.notasapp.ui.createnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udv.notasapp.data.remote.dto.Nota
import com.udv.notasapp.data.repository.NotasRepository
import com.udv.notasapp.util.Resource
import kotlinx.coroutines.launch

class CreateNoteViewModel(private val notasRepository: NotasRepository) : ViewModel() {

    private val _createState = MutableLiveData<Resource<Nota>>()
    val createState: LiveData<Resource<Nota>> = _createState

    companion object {
        private const val TITULO_MAX_LENGTH = 100
    }

    fun crearNota(titulo: String, contenido: String) {
        val tituloTrim = titulo.trim()
        val contenidoTrim = contenido.trim()

        // Validaciones extra además de "no vacío": longitud razonable del título.
        if (tituloTrim.isEmpty() || contenidoTrim.isEmpty()) {
            _createState.value = Resource.Error("El título y el contenido no pueden estar vacíos")
            return
        }
        if (tituloTrim.length > TITULO_MAX_LENGTH) {
            _createState.value = Resource.Error("El título no puede superar los $TITULO_MAX_LENGTH caracteres")
            return
        }

        _createState.value = Resource.Loading
        viewModelScope.launch {
            _createState.value = notasRepository.crearNota(tituloTrim, contenidoTrim)
        }
    }
}
