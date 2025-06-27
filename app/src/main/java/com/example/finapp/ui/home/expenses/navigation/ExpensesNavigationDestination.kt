package com.example.finapp.ui.home.expenses.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ExpensesNavigationDestination {
    @Serializable
    data object ExpensesHistory : ExpensesNavigationDestination()
}
