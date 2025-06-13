package com.example.finapp.home.account.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.home.account.view.AccountScreenUiState
import com.example.finapp.home.account.view.BalanceItemUiState
import com.example.finapp.home.account.view.CurrencyItemUiState

class AccountViewModel : ViewModel() {
    private val mockBalance = BalanceItemUiState(
        totalFormatted = "-670 000 ₽"
    )
    private val mockCurrency = CurrencyItemUiState(
        currency = "₽"
    )

    private val _uiState = mutableStateOf(
        AccountScreenUiState(
            balance  = mockBalance,
            currency = mockCurrency
        )
    )
    val uiState: State<AccountScreenUiState> = _uiState
}