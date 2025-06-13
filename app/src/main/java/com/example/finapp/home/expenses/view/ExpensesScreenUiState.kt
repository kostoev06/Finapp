package com.example.finapp.home.expenses.view

data class ExpensesScreenUiState(
    val summary: ExpensesSumUiState,
    val items: List<ExpenseUiState>
)
