package com.finapp.finapp.theme.di


import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MainViewModelsModule::class,
        MainViewModelsFactoryModule::class
    ]
)
@MainScope
interface FeatureMainComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureMainComponent
    }

    @Module(subcomponents = [FeatureMainComponent::class])
    interface InstallationModule
}