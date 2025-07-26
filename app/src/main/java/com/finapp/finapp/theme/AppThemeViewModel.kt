package com.finapp.finapp.theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AppThemeViewModel @AssistedInject constructor(
    repo: ThemeSettingsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val themeSettings = repo.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AppThemeViewModel>
}