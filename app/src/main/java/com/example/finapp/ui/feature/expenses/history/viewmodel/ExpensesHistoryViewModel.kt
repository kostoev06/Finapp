package com.example.finapp.ui.feature.expenses.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.ui.feature.expenses.history.ExpensesHistoryScreenUiState
import com.example.finapp.ui.feature.expenses.history.ExpensesHistorySumUiState
import com.example.finapp.ui.feature.expenses.history.asExpensesHistoryItemUiState
import com.example.finapp.ui.utils.toFormattedString
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для экрана истории расходов.
 */
class ExpensesHistoryViewModel(
    private val transactionsRepository: TransactionsRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesHistoryScreenUiState())
    val uiState: StateFlow<ExpensesHistoryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            currencyRepository.currency.collect { currency ->
                _uiState.update {
                    it.copy(
                        currency = currency
                    )
                }
            }
        }

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
                    .toFormattedString()
                _uiState.update {
                    it.copy(
                        summary = ExpensesHistorySumUiState(totalAmount = transactionsSum),
                        items = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .sortedByDescending { transaction -> transaction.transactionDate }
                            .map { transaction -> transaction.asExpensesHistoryItemUiState() }.toImmutableList()
                    )
                }
            }
        }
    }
}
