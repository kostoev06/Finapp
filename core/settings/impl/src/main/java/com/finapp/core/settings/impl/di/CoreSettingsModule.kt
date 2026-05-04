package com.finapp.core.settings.impl.di

import com.finapp.core.settings.api.repository.HapticsSettingsRepository
import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import com.finapp.core.settings.impl.repository.HapticsSettingsRepositoryImpl
import com.finapp.core.settings.impl.repository.LanguageRepositoryImpl
import com.finapp.core.settings.impl.repository.PasscodeRepositoryImpl
import com.finapp.core.settings.impl.repository.SoundSettingsRepositoryImpl
import com.finapp.core.settings.impl.repository.ThemeSettingsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [SettingsDataStoresModule::class])
interface CoreSettingsModule {
    @Binds
    fun bindCategoryRepository(impl: ThemeSettingsRepositoryImpl): ThemeSettingsRepository

    @Binds
    fun bindPasscodeRepository(impl: PasscodeRepositoryImpl): PasscodeRepository

    @Binds
    fun bindLanguageRepository(impl: LanguageRepositoryImpl): LanguageRepository

    @Binds
    fun bindSoundSettingsRepository(impl: SoundSettingsRepositoryImpl): SoundSettingsRepository

    @Binds
    fun bindHapticsSettingsRepository(impl: HapticsSettingsRepositoryImpl): HapticsSettingsRepository
}
