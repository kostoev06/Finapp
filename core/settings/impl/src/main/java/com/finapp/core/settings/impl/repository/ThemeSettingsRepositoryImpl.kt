package com.finapp.core.settings.impl.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.finapp.core.settings.api.model.BrandColorOption
import com.finapp.core.settings.api.model.ThemeMode
import com.finapp.core.settings.api.model.ThemeSettings
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import com.finapp.core.settings.impl.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "ui_settings"
private val Context.dataStore by preferencesDataStore(STORE_NAME)

@Singleton
class ThemeSettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : ThemeSettingsRepository {

    override val settings: Flow<ThemeSettings> =
        context.dataStore.data.map { prefs ->
            val mode  = prefs[DataStoreKeys.THEME_MODE]?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM
            val color = prefs[DataStoreKeys.BRAND_COLOR]?.let { BrandColorOption.valueOf(it) } ?: BrandColorOption.GREEN
            ThemeSettings(mode, color)
        }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[DataStoreKeys.THEME_MODE] = mode.name }
    }

    override suspend fun setBrandColor(color: BrandColorOption) {
        context.dataStore.edit { it[DataStoreKeys.BRAND_COLOR] = color.name }
    }
}