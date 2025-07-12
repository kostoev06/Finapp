package com.finapp.feature.home.di

import androidx.lifecycle.ViewModelProvider
import com.finapp.feature.account.di.FeatureAccountComponent
import dagger.Module
import dagger.Subcomponent

@Subcomponent(
    modules = [
        HomeViewModelsModule::class,
        HomeViewModelsFactoryModule::class,
    ]
)
@FeatureHomeScope
interface FeatureHomeComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureHomeComponent
    }

    @Module(subcomponents = [FeatureHomeComponent::class])
    interface InstallationModule
}