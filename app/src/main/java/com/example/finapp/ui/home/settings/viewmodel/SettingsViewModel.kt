package com.example.finapp.ui.home.settings.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.ui.home.settings.component.SettingsScreenUiState

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
