package com.udv.notasapp.ui.noteslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udv.notasapp.data.remote.dto.Nota
import com.udv.notasapp.data.repository.NotasRepository
import com.udv.notasapp.util.Resource
import kotlinx.coroutines.launch

class NotesListViewModel(private val notasRepository: NotasRepository) : ViewModel() {

    private val _notasState = MutableLiveData<Resource<List<Nota>>>()
    val notasState: LiveData<Resource<List<Nota>>> = _notasState

    fun cargarNotas() {
        _notasState.value = Resource.Loading
        viewModelScope.launch {
            _notasState.value = notasRepository.obtenerNotas()
        }
    }
}
