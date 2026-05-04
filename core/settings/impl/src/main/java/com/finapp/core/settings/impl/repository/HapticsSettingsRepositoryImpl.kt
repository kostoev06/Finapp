package com.finapp.core.settings.impl.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.finapp.core.settings.api.repository.HapticsSettingsRepository
import com.finapp.core.settings.impl.DataStoreKeys
import com.finapp.core.settings.impl.di.HapticsSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticsSettingsRepositoryImpl @Inject constructor(
    @HapticsSettingsDataStore private val dataStore: DataStore<Preferences>
) : HapticsSettingsRepository {

    override val enabled: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[DataStoreKeys.HAPTICS_ENABLED] ?: true
        }

    override suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[DataStoreKeys.HAPTICS_ENABLED] = enabled }
    }
}
