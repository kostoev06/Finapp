package com.finapp.feature.settings.sync

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.data.api.repository.SyncStatusRepository
import com.finapp.core.work.transaction.SyncTransactionScheduler
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SyncViewModel @AssistedInject constructor(
    syncStatusRepository: SyncStatusRepository,
    private val scheduler: SyncTransactionScheduler,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = syncStatusRepository.lastSyncEpoch
        .map { SyncScreenUiState(lastSyncEpochSeconds = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SyncScreenUiState())

    fun onSyncClick() {
        scheduler.scheduleOneShot()
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<SyncViewModel>
}
