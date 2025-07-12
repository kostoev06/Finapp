package com.finapp.feature.income.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [IncomeViewModelsModule::class, IncomeViewModelsFactoryModule::class])
@FeatureIncomeScope
interface FeatureIncomeComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureIncomeComponent
    }

    @Module(subcomponents = [FeatureIncomeComponent::class])
    interface InstallationModule
}