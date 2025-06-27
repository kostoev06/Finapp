package com.example.finapp.ui.home.expenses.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl

class ExpensesHistoryViewModelFactory(
    private val transactionsRepository: TransactionsRepository = TransactionsRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesHistoryViewModel(transactionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
