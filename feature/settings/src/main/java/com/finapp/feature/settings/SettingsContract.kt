package com.finapp.feature.settings

import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.ThemeMode

/**
 * UI-состояние экрана настроек.
 */
data class SettingsScreenUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val brandColor: BrandColorOption = BrandColorOption.GREEN
)
