package com.example.finapp.ui.feature.account.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.ui.feature.account.AccountScreenUiState
import com.example.finapp.ui.feature.account.BalanceItemUiState
import com.example.finapp.ui.feature.account.CurrencyItemUiState

/**
 * ViewModel для экрана счета.
 */
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
