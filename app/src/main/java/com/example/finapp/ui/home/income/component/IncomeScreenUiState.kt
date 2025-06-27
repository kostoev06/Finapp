package com.example.finapp.ui.home.income.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI-состояние экрана доходов.
 */
data class IncomeScreenUiState(
    val summary: IncomeSumUiState = IncomeSumUiState(),
    val items: ImmutableList<IncomeItemUiState> = persistentListOf()
)
