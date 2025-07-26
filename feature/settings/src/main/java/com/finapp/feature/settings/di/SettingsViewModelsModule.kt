package com.finapp.feature.settings.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.feature.settings.SettingsViewModel
import com.finapp.feature.settings.about.AboutViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettings(factory: SettingsViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AboutViewModel::class)
    fun bindAbout(factory: AboutViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}