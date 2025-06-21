package com.example.finapp.ui.home.account.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.ui.home.account.component.AccountScreenUiState
import com.example.finapp.ui.home.account.component.BalanceItemUiState
import com.example.finapp.ui.home.account.component.CurrencyItemUiState

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