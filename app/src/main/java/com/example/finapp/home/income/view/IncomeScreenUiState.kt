package com.example.finapp.home.income.view

data class IncomeScreenUiState(
    val summary: IncomeSumUiState,
    val items: List<IncomeItemUiState>
)
