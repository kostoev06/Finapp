package com.example.finapp.ui.feature.income

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние элемента дохода.
 */
data class IncomeItemUiState(
    val title: String,
    val amountFormatted: String
)

/**
 * UI-состояние экрана доходов.
 */
data class IncomeScreenUiState(
    val summary: IncomeSumUiState = IncomeSumUiState(),
    val items: ImmutableList<IncomeItemUiState> = persistentListOf()
)

/**
 * UI-состояние элемента суммы доходов.
 */
data class IncomeSumUiState(
    val totalFormatted: String = "0 ₽"
)