package com.finapp.feature.home.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.feature.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHome(factory: HomeViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}