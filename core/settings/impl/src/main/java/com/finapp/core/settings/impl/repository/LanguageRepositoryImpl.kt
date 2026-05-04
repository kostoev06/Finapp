package com.finapp.core.settings.impl.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.finapp.core.settings.api.model.LanguageOption
import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.core.settings.impl.DataStoreKeys
import com.finapp.core.settings.impl.di.LanguageSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepositoryImpl @Inject constructor(
    @LanguageSettingsDataStore private val dataStore: DataStore<Preferences>
) : LanguageRepository {

    override val language: Flow<LanguageOption> =
        dataStore.data.map { prefs ->
            prefs[DataStoreKeys.LANGUAGE]
                ?.let { runCatching { LanguageOption.valueOf(it) }.getOrNull() }
                ?: LanguageOption.fromSystem(Locale.getDefault().language)
        }

    override suspend fun set(option: LanguageOption) {
        dataStore.edit { it[DataStoreKeys.LANGUAGE] = option.name }
    }
}
