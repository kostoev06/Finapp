package com.example.finapp.ui.home.expenses.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.data.repository.impl.TransactionsRepositoryImpl
import com.example.finapp.ui.home.expenses.history.component.ExpensesHistoryScreenUiState
import com.example.finapp.ui.home.expenses.history.component.ExpensesHistorySumUiState
import com.example.finapp.ui.home.expenses.history.component.toUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExpensesHistoryViewModel : ViewModel() {
    private val transactionsRepository: TransactionsRepository = TransactionsRepositoryImpl()

    private val _uiState = MutableStateFlow(ExpensesHistoryScreenUiState())
    val uiState: StateFlow<ExpensesHistoryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchTransactions()
        }
    }

    fun onChooseStartDate(startDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(startDate = startDate)
            }
            fetchTransactions()
        }
    }

    fun onChooseEndDate(endDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(endDate = endDate)
            }
            fetchTransactions()
        }
    }

    private suspend fun fetchTransactions() {
        transactionsRepository.getTransactions(
            startDate = _uiState.value.startDate.toString(),
            endDate = _uiState.value.endDate.toString()
        ).handleOutcome {
            onSuccess {
                val transactionsSum = data
                    .filter { transaction -> !transaction.category.isIncome }
                    .sumOf { it.amount }
                    .stripTrailingZeros()
                    .toPlainString()
                _uiState.update {
                    it.copy(
                        summary = ExpensesHistorySumUiState(totalFormatted = "$transactionsSum ₽"),
                        items = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .map { transaction -> transaction.toUiState() }.toImmutableList()
                    )
                }
            }
        }
    }
}