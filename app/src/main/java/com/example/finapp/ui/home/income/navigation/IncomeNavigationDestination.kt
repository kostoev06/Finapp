package com.example.finapp.ui.home.income.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана доходов.
 */
@Serializable
sealed class IncomeNavigationDestination {
    @Serializable
    data object IncomeHistory : IncomeNavigationDestination()
}
