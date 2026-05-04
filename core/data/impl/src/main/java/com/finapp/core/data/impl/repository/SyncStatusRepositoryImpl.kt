package com.finapp.core.data.impl.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.finapp.core.data.api.repository.SyncStatusRepository
import com.finapp.core.data.impl.di.SyncStatusDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncStatusRepositoryImpl @Inject constructor(
    @SyncStatusDataStore private val dataStore: DataStore<Preferences>
) : SyncStatusRepository {

    override val lastSyncEpoch: Flow<Long?> =
        dataStore.data.map { prefs -> prefs[LAST_SYNC]?.takeIf { it > 0L } }

    override suspend fun markSynced(epochSeconds: Long) {
        dataStore.edit { it[LAST_SYNC] = epochSeconds }
    }

    private companion object {
        val LAST_SYNC = longPreferencesKey("last_sync")
    }
}
