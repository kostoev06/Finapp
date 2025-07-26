package com.finapp.core.settings.api.repository

import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.ThemeMode
import com.finapp.core.settings.api.model.ThemeSettings
import kotlinx.coroutines.flow.Flow

interface ThemeSettingsRepository {
    val settings: Flow<ThemeSettings>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setBrandColor(color: BrandColorOption)
}
