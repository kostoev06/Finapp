package com.example.finapp.ui.home.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.ui.home.expenses.component.ExpensesScreenUiState
import com.example.finapp.ui.home.expenses.component.ExpensesSumUiState
import com.example.finapp.ui.home.expenses.component.toUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


/**
 * ViewModel для экрана расходов.
 */
class ExpensesViewModel(
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesScreenUiState())
    val uiState: StateFlow<ExpensesScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val today = LocalDate.now().toString()

            transactionsRepository.getTransactions(startDate = today, endDate = today)
                .handleOutcome {
                    onSuccess {
                        val transactionsSum = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .sumOf { it.amount }
                            .stripTrailingZeros()
                            .toPlainString()
                        _uiState.update {
                            it.copy(
                                summary = ExpensesSumUiState(totalFormatted = "$transactionsSum ₽"),
                                items = data
                                    .filter { transaction -> !transaction.category.isIncome }
                                    .map { transaction -> transaction.toUiState() }
                                    .toImmutableList()
                            )
                        }
                    }
                }
        }
    }
}
