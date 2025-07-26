package com.finapp.feature.account.homepage

import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние экрана счета.
 */
data class AccountScreenUiState(
    val balanceState: BalanceItemUiState = BalanceItemUiState(),
    val currencyState: CurrencyItemUiState = CurrencyItemUiState(),
    val profitItemListUiState: ImmutableList<ProfitItemUiState> = persistentListOf(),
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

data class ProfitItemUiState(
    val title: String,
    val amount: Float
)