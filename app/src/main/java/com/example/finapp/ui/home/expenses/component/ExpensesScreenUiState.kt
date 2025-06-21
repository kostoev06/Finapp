package com.example.finapp.ui.home.expenses.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ExpensesScreenUiState(
    val summary: ExpensesSumUiState = ExpensesSumUiState(),
    val items: ImmutableList<ExpensesItemUiState> = persistentListOf()
)
