package com.example.finapp.home.income.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.finapp.home.income.view.IncomeItemUiState
import com.example.finapp.home.income.view.IncomeScreenUiState
import com.example.finapp.home.income.view.IncomeSumUiState

class IncomeViewModel : ViewModel() {

    private val mockSummary = IncomeSumUiState(
        totalFormatted = "600 000 ₽"
    )

    private val mockItems = listOf(
        IncomeItemUiState("Зарплата",   "500 000 ₽"),
        IncomeItemUiState("Подработка", "100 000 ₽")
    )

    private val _uiState = mutableStateOf(
        IncomeScreenUiState(
            summary = mockSummary,
            items   = mockItems
        )
    )
    val uiState: State<IncomeScreenUiState> = _uiState
}