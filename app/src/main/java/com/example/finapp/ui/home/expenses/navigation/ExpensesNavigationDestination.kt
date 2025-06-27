package com.example.finapp.ui.home.expenses.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана расходов.
 */
@Serializable
sealed class ExpensesNavigationDestination {
    @Serializable
    data object ExpensesHistory : ExpensesNavigationDestination()
}
