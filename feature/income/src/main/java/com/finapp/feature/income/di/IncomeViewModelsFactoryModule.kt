package com.finapp.feature.income.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finapp.feature.common.di.DaggerViewModelFactory
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.Module
import dagger.Provides

@Module
class IncomeViewModelsFactoryModule {
    @Provides
    @FeatureIncomeScope
    fun provideVmFactory(
        assisted: Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModelAssistedFactory<out ViewModel>>
    ): ViewModelProvider.Factory = DaggerViewModelFactory(assisted)
}