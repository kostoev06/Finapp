package com.finapp.feature.income.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.feature.income.IncomeViewModel
import com.finapp.feature.income.edit.IncomeEditViewModel
import com.finapp.feature.income.history.IncomeHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface IncomeViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    fun bindIncome(factory: IncomeViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(IncomeEditViewModel::class)
    fun bindIncomeEdit(factory: IncomeEditViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(IncomeHistoryViewModel::class)
    fun bindIncomeHistory(factory: IncomeHistoryViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}