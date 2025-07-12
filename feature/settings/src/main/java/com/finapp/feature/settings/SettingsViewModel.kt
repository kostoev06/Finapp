package com.finapp.feature.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * ViewModel для экрана настроек.
 */
class SettingsViewModel : ViewModel() {
    private val _uiState = mutableStateOf(
        SettingsScreenUiState(isDarkThemeEnabled = false)
    )
    val uiState: State<SettingsScreenUiState> = _uiState

    fun onDarkThemeToggle(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkThemeEnabled = enabled)
    }
}