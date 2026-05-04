package com.finapp.core.data.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier и провайд DataStore для [com.finapp.core.data.api.repository.SyncStatusRepository].
 *
 * Имя файла — `settings` — оставлено намеренно: ранее этот общий DataStore использовался для
 * единственного ключа `last_sync`, и переименование сломало бы существующие установки. Здесь
 * он живёт изолированно, репозиторий получает уже готовый объект.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SyncStatusDataStore

private val Context.syncStatusDataStore by preferencesDataStore("settings")

@Module
object SyncStatusDataStoreModule {
    @Provides
    @Singleton
    @SyncStatusDataStore
    fun provideSyncStatusDataStore(context: Context): DataStore<Preferences> =
        context.syncStatusDataStore
}
