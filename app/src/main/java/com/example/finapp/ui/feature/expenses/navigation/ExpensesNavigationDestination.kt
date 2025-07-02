package com.example.finapp.ui.feature.expenses.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана расходов.
 */
@Serializable
sealed class ExpensesNavigationDestination {
    @Serializable
    data object ExpensesHistory : ExpensesNavigationDestination()
}
