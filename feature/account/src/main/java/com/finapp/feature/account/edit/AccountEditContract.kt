package com.finapp.feature.account.edit

import com.finapp.core.data.api.model.CurrencyCode

data class AccountEditScreenUiState(
    val nameFieldState: NameFieldUiState = NameFieldUiState(),
    val balanceFieldState: BalanceFieldUiState = BalanceFieldUiState(),
    val currencyFieldState: CurrencyFieldUiState = CurrencyFieldUiState(),
    val isCurrencySheetVisible: Boolean = false
)

/** Состояние текстового поля «Название счёта» */
data class NameFieldUiState(
    val text: String = ""
)

/** Состояние текстового поля «Баланс» */
data class BalanceFieldUiState(
    val text: String = ""
)

/** Состояние элемента «Валюта» */
data class CurrencyFieldUiState(
    val currency: CurrencyCode = CurrencyCode.RUB
)