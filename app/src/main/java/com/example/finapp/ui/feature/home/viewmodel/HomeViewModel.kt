package com.example.finapp.ui.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.AccountRepository
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl
import kotlinx.coroutines.launch

class HomeViewModel(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            accountRepository.getAccount().handleOutcome {
                onSuccess {
                    currencyRepository.setCurrency(data.currency)
                }
            }
        }
    }
}