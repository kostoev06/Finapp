package com.example.finapp.ui.feature.income.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl

/**
 * Фабрика для создания IncomeHistoryViewModel с передачей зависимостей.
 */
class IncomeHistoryViewModelFactory(
    private val transactionsRepository: TransactionsRepository = TransactionsRepositoryImpl(),
    private val currencyRepository: CurrencyRepository = CurrencyRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeHistoryViewModel(transactionsRepository, currencyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
