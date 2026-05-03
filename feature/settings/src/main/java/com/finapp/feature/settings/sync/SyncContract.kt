package com.finapp.feature.settings.sync

/**
 * UI-состояние экрана синхронизации.
 *
 * @property lastSyncEpochSeconds время последней успешной синхронизации в секундах эпохи,
 *   `null` если синхронизаций ещё не было.
 */
data class SyncScreenUiState(
    val lastSyncEpochSeconds: Long? = null
)
