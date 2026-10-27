package com.udv.notasapp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory genérica para no tener que escribir una clase Factory distinta por cada ViewModel.
 * Simplemente recibe un lambda que construye el ViewModel con sus dependencias (repository, etc.).
 */
class ViewModelFactory<VM : ViewModel>(private val creator: () -> VM) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
