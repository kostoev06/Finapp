package com.finapp.core.data.api.repository

import kotlinx.coroutines.flow.Flow

/**
 * Состояние последней синхронизации с сервером.
 *
 * Воркер пишет момент успешной синхронизации через [markSynced]; экраны (Sync, Account)
 * читают [lastSyncEpoch], чтобы показать пользователю «последняя синхронизация N времени
 * назад». Хранилище не утекает наружу — в перспективе реализация может уехать в БД или в
 * сервис без правок потребителей.
 */
interface SyncStatusRepository {
    /** Unix-секунды успешной синхронизации; `null` — синхронизаций ещё не было. */
    val lastSyncEpoch: Flow<Long?>

    /** Отметить успешную синхронизацию текущим моментом (Unix-секунды). */
    suspend fun markSynced(epochSeconds: Long)
}
