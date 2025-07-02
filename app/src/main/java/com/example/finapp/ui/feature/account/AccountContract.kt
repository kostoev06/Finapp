package com.example.finapp.ui.feature.account

/**
 * UI-состояние экрана счета.
 */
data class AccountScreenUiState(
    val balance: BalanceItemUiState,
    val currency: CurrencyItemUiState
)

/**
 * UI-состояние элемента баланса.
 */
data class BalanceItemUiState(
    val totalFormatted: String
)

/**
 * UI-состояние элемента валюты.
 */
data class CurrencyItemUiState(
    val currency: String
)