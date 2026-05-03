package com.finapp.feature.settings.haptics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.repository.HapticsSettingsRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HapticsViewModel @AssistedInject constructor(
    private val repository: HapticsSettingsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = repository.enabled
        .map { HapticsScreenUiState(enabled = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HapticsScreenUiState())

    fun onToggle(enabled: Boolean) {
        viewModelScope.launch { repository.setEnabled(enabled) }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<HapticsViewModel>
}
