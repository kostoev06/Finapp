package com.example.finapp.ui.feature.account.edit

data class EditAccountScreenUiState(
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
    val currency: String = "RUB"
)