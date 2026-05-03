package com.finapp.feature.settings.sound

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SoundViewModel @AssistedInject constructor(
    private val repository: SoundSettingsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = repository.enabled
        .map { SoundScreenUiState(enabled = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SoundScreenUiState())

    fun onToggle(enabled: Boolean) {
        viewModelScope.launch { repository.setEnabled(enabled) }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SoundViewModel>
}
