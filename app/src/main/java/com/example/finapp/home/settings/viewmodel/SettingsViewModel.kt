package com.example.finapp.home.settings.viewmodel


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.home.settings.view.SettingsScreenUiState

class SettingsViewModel : ViewModel() {
    private val _uiState = mutableStateOf(
        SettingsScreenUiState(isDarkThemeEnabled = false)
    )
    val uiState: State<SettingsScreenUiState> = _uiState

    fun onDarkThemeToggle(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkThemeEnabled = enabled)
    }
}