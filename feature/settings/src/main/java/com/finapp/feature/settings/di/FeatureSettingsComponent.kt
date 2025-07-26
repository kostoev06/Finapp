package com.finapp.feature.settings.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [SettingsViewModelsModule::class, SettingsViewModelsFactoryModule::class])
@FeatureSettingsScope
interface FeatureSettingsComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureSettingsComponent
    }

    @Module(subcomponents = [FeatureSettingsComponent::class])
    interface InstallationModule
}