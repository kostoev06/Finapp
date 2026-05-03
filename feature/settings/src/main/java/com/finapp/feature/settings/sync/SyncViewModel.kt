package com.finapp.feature.settings.sync

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.work.transaction.SyncTransactionScheduler
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private val LAST_SYNC = longPreferencesKey("last_sync")

class SyncViewModel @AssistedInject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scheduler: SyncTransactionScheduler,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = dataStore.data
        .map { prefs ->
            // 0L означает «синхронизаций ещё не было» — SyncTransactionWorker пишет в этот ключ
            // только после успешной выгрузки.
            SyncScreenUiState(lastSyncEpochSeconds = prefs[LAST_SYNC]?.takeIf { it > 0L })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SyncScreenUiState())

    fun onSyncClick() {
        scheduler.scheduleOneShot()
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SyncViewModel>
}
