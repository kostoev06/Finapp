package com.finapp.core.settings.impl.di

import javax.inject.Qualifier

/**
 * Qualifier'ы для DataStore-инстансов настроек.
 *
 * Каждый репозиторий настроек живёт в своём файле DataStore — чтобы изменения в одной
 * группе настроек не блокировали чтение/запись других. Соответствующие провайды лежат в
 * [SettingsDataStoresModule]; репозитории получают `DataStore<Preferences>` через DI и
 * не знают ни про `Context`, ни про имя файла.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UiSettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LanguageSettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SoundSettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HapticsSettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PasscodeSettingsDataStore
