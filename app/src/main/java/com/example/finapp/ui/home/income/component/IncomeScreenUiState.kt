package com.example.finapp.ui.home.income.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class IncomeScreenUiState(
    val summary: IncomeSumUiState = IncomeSumUiState(),
    val items: ImmutableList<IncomeItemUiState> = persistentListOf()
)
