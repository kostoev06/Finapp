package com.finapp.feature.account.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [AccountViewModelsModule::class, AccountViewModelsFactoryModule::class])
@FeatureAccountScope
interface FeatureAccountComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureAccountComponent
    }

    @Module(subcomponents = [FeatureAccountComponent::class])
    interface InstallationModule
}