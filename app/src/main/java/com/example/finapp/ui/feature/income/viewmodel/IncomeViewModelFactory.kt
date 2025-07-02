package com.example.finapp.ui.feature.income.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl

/**
 * Фабрика для создания IncomeViewModel с передачей зависимостей.
 */
class IncomeViewModelFactory(
    private val transactionsRepository: TransactionsRepository = TransactionsRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(transactionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
