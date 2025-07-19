package com.finapp.feature.expenses.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана расходов.
 */
@Serializable
sealed class ExpensesNavigationDestination {
    @Serializable
    data object ExpensesHistory : ExpensesNavigationDestination()
    @Serializable
    data object ExpensesAnalysis : ExpensesNavigationDestination()
    @Serializable
    data class EditExpense(val expenseId: Long?) : ExpensesNavigationDestination()
}
