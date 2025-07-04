package com.example.finapp.ui.feature.account.edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.impl.AccountRepositoryImpl
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl

/**
 * Фабрика для создания EditAccountViewModel с передачей зависимостей.
 */
class EditAccountViewModelFactory(
    private val accountRepository: AccountRepository = AccountRepositoryImpl(),
    private val currencyRepository: CurrencyRepository = CurrencyRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAccountViewModel(accountRepository, currencyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}