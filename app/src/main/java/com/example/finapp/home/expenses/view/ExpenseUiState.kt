package com.example.finapp.home.expenses.view

data class ExpenseUiState(
    val leadingSymbols: String? = null,
    val title: String,
    val subtitle: String? = null,
    val amountFormatted: String
)
