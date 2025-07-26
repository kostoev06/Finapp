package com.finapp.core.settings.impl.di

import com.finapp.core.settings.api.repository.ThemeSettingsRepository
import com.finapp.core.settings.impl.repository.ThemeSettingsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface CoreSettingsModule {
    @Binds
    fun bindCategoryRepository(impl: ThemeSettingsRepositoryImpl): ThemeSettingsRepository
}