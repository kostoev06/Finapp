package com.finapp.feature.income.navigation

import kotlinx.serialization.Serializable

/**
 * Навигационный объект-назначение для экрана доходов.
 */
@Serializable
sealed class IncomeNavigationDestination {
    @Serializable
    data object IncomeHistory : IncomeNavigationDestination()
    @Serializable
    data object IncomeAnalysis : IncomeNavigationDestination()
    @Serializable
    data class EditIncome(val incomeId: Long?) : IncomeNavigationDestination()
}
