package com.finapp.feature.income.history

import androidx.compose.runtime.Immutable
import com.finapp.core.data.api.model.CurrencyCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

/**
 * UI-состояние элемента истории доходов.
 */
data class IncomeHistoryItemUiState(
    val id: Long,
    val title: String,
    val subtitle: String? = null,
    val amount: String,
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
    val items: ImmutableList<IncomeHistoryItemUiState> = persistentListOf(),
    val currency: CurrencyCode = CurrencyCode.RUB
)

/**
 * UI-состояние элемента суммы доходов из истории.
 */
data class IncomeHistorySumUiState(
    val totalAmount: String = "0"
)