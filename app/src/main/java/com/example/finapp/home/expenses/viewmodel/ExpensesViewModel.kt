package com.example.finapp.home.expenses.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.home.expenses.view.ExpenseUiState
import com.example.finapp.home.expenses.view.ExpensesScreenUiState
import com.example.finapp.home.expenses.view.ExpensesSumUiState


class ExpensesViewModel : ViewModel() {
    private val summary = ExpensesSumUiState(
        totalFormatted = "436 558 ₽"
    )

    private val mockItems = listOf(
        ExpenseUiState("🏡", "Аренда квартиры", null, "100 000 ₽"),
        ExpenseUiState("👗", "Одежда", null, "100 000 ₽"),
        ExpenseUiState("🐶", "На собачку", "Джек", "100 000 ₽"),
        ExpenseUiState("🐶", "На собачку", "Энни", "100 000 ₽"),
        ExpenseUiState("РК", "Ремонт квартиры", null, "100 000 ₽"),
        ExpenseUiState("🍭", "Продукты", null, "100 000 ₽"),
        ExpenseUiState("🏋️", "Спортзал", null, "100 000 ₽"),
        ExpenseUiState("💊", "Медицина", null, "100 000 ₽")
    )

    private val _uiState = mutableStateOf(
        ExpensesScreenUiState(
            summary = summary,
            items = mockItems
        )
    )
    val uiState: State<ExpensesScreenUiState> = _uiState
}
