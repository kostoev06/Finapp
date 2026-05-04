package com.finapp.core.data.impl.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.data.impl.di.CurrencyDataStore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    @CurrencyDataStore private val dataStore: DataStore<Preferences>
) : CurrencyRepository {

    override val currency: Flow<String> =
        dataStore.data.map { prefs -> prefs[CURRENCY_KEY] ?: DEFAULT_CURRENCY }

    override suspend fun setCurrency(currency: String) {
        dataStore.edit { it[CURRENCY_KEY] = currency }
    }

    private companion object {
        val CURRENCY_KEY = stringPreferencesKey("currency")
        const val DEFAULT_CURRENCY = "RUB"
    }
}
