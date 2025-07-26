package com.finapp.feature.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.ThemeMode
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана настроек.
 */
class SettingsViewModel @AssistedInject constructor(
    private val repo: ThemeSettingsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = repo.settings
        .map { SettingsScreenUiState(it.themeMode, it.brandColor) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenUiState())

    fun onDarkThemeToggle(enabled: Boolean) {
        viewModelScope.launch {
            repo.setThemeMode(if (enabled) ThemeMode.DARK else ThemeMode.LIGHT)
        }
    }

    fun onThemeModeSelect(mode: ThemeMode) {
        viewModelScope.launch { repo.setThemeMode(mode) }
    }

    fun onBrandColorSelect(option: BrandColorOption) {
        viewModelScope.launch { repo.setBrandColor(option) }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SettingsViewModel>
}