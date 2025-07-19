package com.finapp.feature.expenses.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.feature.expenses.homepage.ExpensesViewModel
import com.finapp.feature.expenses.analysis.ExpensesAnalysisViewModel
import com.finapp.feature.expenses.edit.ExpenseEditViewModel
import com.finapp.feature.expenses.history.ExpensesHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExpensesViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindExpenses(factory: ExpensesViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseEditViewModel::class)
    fun bindExpenseEdit(factory: ExpenseEditViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    fun bindExpensesHistory(factory: ExpensesHistoryViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesAnalysisViewModel::class)
    fun bindExpensesAnalysis(factory: ExpensesAnalysisViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}