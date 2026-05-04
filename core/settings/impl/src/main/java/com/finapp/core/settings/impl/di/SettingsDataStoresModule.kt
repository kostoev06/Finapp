package com.finapp.core.settings.impl.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Провайды DataStore-инстансов для настроек.
 *
 * Делегаты [preferencesDataStore] держат singleton на файл — повторный вызов на том же
 * `Context` отдаёт тот же объект, поэтому здесь они объявлены на уровне модуля и
 * "кэшируются" Dagger'ом через `@Singleton`. Имена файлов — единственное место, где они
 * прописаны; репозитории об именах не знают.
 */
private val Context.uiSettingsDataStore by preferencesDataStore("ui_settings")
private val Context.languageSettingsDataStore by preferencesDataStore("language_settings")
private val Context.soundSettingsDataStore by preferencesDataStore("sound_settings")
private val Context.hapticsSettingsDataStore by preferencesDataStore("haptics_settings")
private val Context.passcodeSettingsDataStore by preferencesDataStore("auth_settings")

@Module
object SettingsDataStoresModule {
    @Provides
    @Singleton
    @UiSettingsDataStore
    fun provideUiSettingsDataStore(context: Context): DataStore<Preferences> =
        context.uiSettingsDataStore

    @Provides
    @Singleton
    @LanguageSettingsDataStore
    fun provideLanguageSettingsDataStore(context: Context): DataStore<Preferences> =
        context.languageSettingsDataStore

    @Provides
    @Singleton
    @SoundSettingsDataStore
    fun provideSoundSettingsDataStore(context: Context): DataStore<Preferences> =
        context.soundSettingsDataStore

    @Provides
    @Singleton
    @HapticsSettingsDataStore
    fun provideHapticsSettingsDataStore(context: Context): DataStore<Preferences> =
        context.hapticsSettingsDataStore

    @Provides
    @Singleton
    @PasscodeSettingsDataStore
    fun providePasscodeSettingsDataStore(context: Context): DataStore<Preferences> =
        context.passcodeSettingsDataStore
}
