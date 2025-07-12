package com.finapp.feature.expenses.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [ExpensesViewModelsModule::class, ExpensesViewModelsFactoryModule::class])
@FeatureExpensesScope
interface FeatureExpensesComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureExpensesComponent
    }

    @Module(subcomponents = [FeatureExpensesComponent::class])
    interface InstallationModule
}