package com.example.finapp.ui.feature.income.history

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние элемента истории доходов.
 */
data class IncomeHistoryItemUiState(
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String,
    val timeText: String
)

/**
 * UI-состояние экрана истории доходов.
 */
@Immutable
data class IncomeHistoryScreenUiState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val summary: IncomeHistorySumUiState = IncomeHistorySumUiState(),
    val items: ImmutableList<IncomeHistoryItemUiState> = persistentListOf()
)

/**
 * UI-состояние элемента суммы доходов из истории.
 */
data class IncomeHistorySumUiState(
    val totalFormatted: String = "0 ₽"
)