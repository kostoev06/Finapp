package com.finapp.feature.expenses.edit

import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDate
import java.time.LocalTime

data class ExpenseEditScreenUiState(
    val expenseId: Long?,
    val accountFieldState: String,
    val currentCategoryState: CategoryUiState,
    val categoriesListState: ImmutableList<CategoryUiState>,
    val isCategoryDialogVisible: Boolean,
    val amountFieldState: String,
    val dateState: LocalDate,
    val timeState: LocalTime,
    val commentFieldState: String,
    val showErrorDialog: Boolean = false,
    val isLoading: Boolean = false,
    val lastErrorText: String? = null
)

data class CategoryUiState(
    val id: Long,
    val name: String
)
