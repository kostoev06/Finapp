package com.finapp.core.settings.impl.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import com.finapp.core.settings.impl.DataStoreKeys
import com.finapp.core.settings.impl.di.SoundSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundSettingsRepositoryImpl @Inject constructor(
    @SoundSettingsDataStore private val dataStore: DataStore<Preferences>
) : SoundSettingsRepository {

    override val enabled: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[DataStoreKeys.SOUND_ENABLED] ?: true
        }

    override suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[DataStoreKeys.SOUND_ENABLED] = enabled }
    }
}
