package com.finapp.core.settings.impl.di

import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.core.settings.api.repository.PasscodeRepository
import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import com.finapp.core.settings.impl.repository.LanguageRepositoryImpl
import com.finapp.core.settings.impl.repository.PasscodeRepositoryImpl
import com.finapp.core.settings.impl.repository.ThemeSettingsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface CoreSettingsModule {
    @Binds
    fun bindCategoryRepository(impl: ThemeSettingsRepositoryImpl): ThemeSettingsRepository

    @Binds
    fun bindPasscodeRepository(impl: PasscodeRepositoryImpl): PasscodeRepository

    @Binds
    fun bindLanguageRepository(impl: LanguageRepositoryImpl): LanguageRepository
}
