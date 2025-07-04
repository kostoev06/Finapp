package com.example.finapp.ui.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.CurrencyRepository
import com.example.finapp.data.repository.impl.CurrencyRepositoryImpl
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.ui.feature.expenses.ExpensesScreenUiState
import com.example.finapp.ui.feature.expenses.ExpensesSumUiState
import com.example.finapp.ui.feature.expenses.asExpensesItemUiState
import com.example.finapp.ui.utils.toFormattedString
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
    private val transactionsRepository: TransactionsRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesScreenUiState())
    val uiState: StateFlow<ExpensesScreenUiState> = _uiState.asStateFlow()

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
            val today = LocalDate.now().toString()

            transactionsRepository.getTransactions(startDate = today, endDate = today)
                .handleOutcome {
                    onSuccess {
                        val transactionsSum = data
                            .filter { transaction -> !transaction.category.isIncome }
                            .sumOf { it.amount }
                            .toFormattedString()
                        _uiState.update {
                            it.copy(
                                summary = ExpensesSumUiState(totalAmount = transactionsSum),
                                items = data
                                    .filter { transaction -> !transaction.category.isIncome }
                                    .map { transaction -> transaction.asExpensesItemUiState() }
                                    .toImmutableList()
                            )
                        }
                    }
                }
        }
    }
}
