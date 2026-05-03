package com.finapp.core.settings.impl.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.finapp.core.settings.api.repository.HapticsSettingsRepository
import com.finapp.core.settings.impl.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "haptics_settings"
private val Context.hapticsDataStore by preferencesDataStore(STORE_NAME)

@Singleton
class HapticsSettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : HapticsSettingsRepository {

    override val enabled: Flow<Boolean> =
        context.hapticsDataStore.data.map { prefs ->
            prefs[DataStoreKeys.HAPTICS_ENABLED] ?: true
        }

    override suspend fun setEnabled(enabled: Boolean) {
        context.hapticsDataStore.edit { it[DataStoreKeys.HAPTICS_ENABLED] = enabled }
    }
}
