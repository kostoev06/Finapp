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
 * Qualifier и провайд DataStore для [com.finapp.core.data.api.repository.CurrencyRepository].
 *
 * Имя файла — `currency_settings`. Изолируем валюту от любых других DataStore-файлов,
 * чтобы никто случайно не записал «соседним ключом» в общий стор.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyDataStore

private val Context.currencyDataStore by preferencesDataStore("currency_settings")

@Module
object CurrencyDataStoreModule {
    @Provides
    @Singleton
    @CurrencyDataStore
    fun provideCurrencyDataStore(context: Context): DataStore<Preferences> =
        context.currencyDataStore
}
