package com.finapp.feature.tags.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [TagsViewModelsModule::class, TagsViewModelsFactoryModule::class])
@FeatureTagsScope
interface FeatureTagsComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureTagsComponent
    }

    @Module(subcomponents = [FeatureTagsComponent::class])
    interface InstallationModule
}