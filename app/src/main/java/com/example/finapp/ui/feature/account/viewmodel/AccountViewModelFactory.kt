package com.example.finapp.ui.feature.account.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.data.repository.impl.AccountRepositoryImpl


/**
 * Фабрика для создания AccountViewModel с передачей зависимостей.
 */
class AccountViewModelFactory(
    private val accountRepository: AccountRepository = AccountRepositoryImpl()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(accountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
