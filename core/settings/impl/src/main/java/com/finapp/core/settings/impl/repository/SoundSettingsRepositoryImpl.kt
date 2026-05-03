package com.finapp.core.settings.impl.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import com.finapp.core.settings.impl.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "sound_settings"
private val Context.soundDataStore by preferencesDataStore(STORE_NAME)

@Singleton
class SoundSettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SoundSettingsRepository {

    override val enabled: Flow<Boolean> =
        context.soundDataStore.data.map { prefs ->
            prefs[DataStoreKeys.SOUND_ENABLED] ?: true
        }

    override suspend fun setEnabled(enabled: Boolean) {
        context.soundDataStore.edit { it[DataStoreKeys.SOUND_ENABLED] = enabled }
    }
}
