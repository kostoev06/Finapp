package com.example.finapp.ui.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.impl.AccountRepositoryImpl
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl

class HomeViewModelFactory(
    private val accountRepository: AccountRepository = AccountRepositoryImpl(),
    private val currencyRepository: CurrencyRepository = CurrencyRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(accountRepository, currencyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}