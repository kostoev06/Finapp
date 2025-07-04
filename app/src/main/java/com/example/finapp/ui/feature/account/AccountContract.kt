package com.example.finapp.ui.feature.account

/**
 * UI-состояние экрана счета.
 */
data class AccountScreenUiState(
    val balanceState: BalanceItemUiState = BalanceItemUiState(),
    val currencyState: CurrencyItemUiState = CurrencyItemUiState()
)

/**
 * UI-состояние элемента баланса.
 */
data class BalanceItemUiState(
    val balance: String = ""
)

/**
 * UI-состояние элемента валюты.
 */
data class CurrencyItemUiState(
    val currency: String = "₽"
)