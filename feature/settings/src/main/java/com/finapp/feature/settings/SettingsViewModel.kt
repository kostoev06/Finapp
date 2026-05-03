package com.finapp.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.LanguageOption
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
    private val languageRepo: LanguageRepository,
    passcodeRepo: PasscodeRepository,
    soundRepo: SoundSettingsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = combine(
        repo.settings,
        passcodeRepo.isSet,
        languageRepo.language,
        soundRepo.enabled
    ) { theme, passcodeIsSet, language, soundEnabled ->
        SettingsScreenUiState(theme.themeMode, theme.brandColor, passcodeIsSet, language, soundEnabled)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsScreenUiState())

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

    fun onLanguageSelect(option: LanguageOption) {
        viewModelScope.launch {
            languageRepo.set(option)
            // Меняет локаль и пересоздаёт текущую Activity, чтобы строки перерисовались.
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(option.tag)
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SettingsViewModel>
}
