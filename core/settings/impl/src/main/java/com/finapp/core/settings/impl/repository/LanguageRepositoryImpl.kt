package com.finapp.core.settings.impl.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.finapp.core.settings.api.model.LanguageOption
import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.core.settings.impl.DataStoreKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

private const val STORE_NAME = "language_settings"
private val Context.languageDataStore by preferencesDataStore(STORE_NAME)

@Singleton
class LanguageRepositoryImpl @Inject constructor(
    private val context: Context
) : LanguageRepository {

    override val language: Flow<LanguageOption> =
        context.languageDataStore.data.map { prefs ->
            prefs[DataStoreKeys.LANGUAGE]
                ?.let { runCatching { LanguageOption.valueOf(it) }.getOrNull() }
                ?: LanguageOption.fromSystem(Locale.getDefault().language)
        }

    override suspend fun set(option: LanguageOption) {
        context.languageDataStore.edit { it[DataStoreKeys.LANGUAGE] = option.name }
    }
}
