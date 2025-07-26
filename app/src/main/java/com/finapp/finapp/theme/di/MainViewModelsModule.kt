package com.finapp.finapp.theme.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.finapp.theme.AppThemeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(AppThemeViewModel::class)
    fun bindAppTheme(factory: AppThemeViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}