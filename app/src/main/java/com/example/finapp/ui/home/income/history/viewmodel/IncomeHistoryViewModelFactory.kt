package com.example.finapp.ui.home.income.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl

/**
 * Фабрика для создания IncomeHistoryViewModel с передачей зависимостей.
 */
class IncomeHistoryViewModelFactory(
    private val transactionsRepository: TransactionsRepository = TransactionsRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeHistoryViewModel(transactionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
