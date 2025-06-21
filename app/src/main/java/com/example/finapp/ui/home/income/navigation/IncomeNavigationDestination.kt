package com.example.finapp.ui.home.income.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class IncomeNavigationDestination {
    @Serializable
    data object IncomeHistory: IncomeNavigationDestination()
}