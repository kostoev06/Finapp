package com.finapp.feature.account.homepage

import com.finapp.core.data.api.model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun List<Transaction>.toDailyProfitItems(): List<ProfitItemUiState> {
    val today = LocalDate.now()
    val startOfMonth = today.withDayOfMonth(1)
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())

    val grouped: Map<LocalDate, List<Transaction>> =
        this.groupBy { it.transactionDate.toLocalDate() }

    val formatter = DateTimeFormatter.ofPattern("dd.MM")

    return generateSequence(startOfMonth) { previous ->
        val next = previous.plusDays(1)
        if (next <= endOfMonth) next else null
    }
        .map { date ->
            val txs = grouped[date].orEmpty()
            val daySum = txs.fold(BigDecimal.ZERO) { acc, tx ->
                if (tx.category.isIncome) acc + tx.amount else acc - tx.amount
            }
            ProfitItemUiState(
                title = date.format(formatter),
                amount = daySum.toFloat()
            )
        }
        .toList()
}
