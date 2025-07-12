package com.finapp.feature.income.edit

import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate
import java.time.LocalTime

data class IncomeEditScreenUiState(
    val expenseId: Long?,
    val accountFieldState: String,
    val currentCategoryState: CategoryUiState,
    val categoriesListState: ImmutableList<CategoryUiState>,
    val isCategoryDialogVisible: Boolean,
    val amountFieldState: String,
    val dateState: LocalDate,
    val timeState: LocalTime,
    val commentFieldState: String
)

data class CategoryUiState(
    val id: Long,
    val name: String
)
