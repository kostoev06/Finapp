package com.finapp.feature.common.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface ViewModelAssistedFactory<VM : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): VM
}
