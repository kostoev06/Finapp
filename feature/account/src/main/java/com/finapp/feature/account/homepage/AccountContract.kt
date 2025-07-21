package com.finapp.feature.account.homepage

import com.finapp.core.data.api.model.CurrencyCode

/**
 * UI-состояние экрана счета.
 */
data class AccountScreenUiState(
    val balanceState: BalanceItemUiState = BalanceItemUiState(),
    val currencyState: CurrencyItemUiState = CurrencyItemUiState(),
    val lastSyncTextState: String = "Никогда"
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
    val currency: CurrencyCode = CurrencyCode.RUB
)