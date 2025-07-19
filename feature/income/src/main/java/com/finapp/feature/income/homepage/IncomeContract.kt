package com.finapp.feature.income.homepage

import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние элемента дохода.
 */
data class IncomeItemUiState(
    val id: Long,
    val title: String,
    val amount: String
)

/**
 * UI-состояние экрана доходов.
 */
data class IncomeScreenUiState(
    val summary: IncomeSumUiState = IncomeSumUiState(),
    val items: ImmutableList<IncomeItemUiState> = persistentListOf(),
    val currency: CurrencyCode = CurrencyCode.RUB
)

/**
 * UI-состояние элемента суммы доходов.
 */
data class IncomeSumUiState(
    val totalAmount: String = "0"
)