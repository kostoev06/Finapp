package com.example.finapp.ui.feature.income.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finapp.data.common.handleOutcome
import com.example.finapp.data.repository.TransactionsRepository
import com.example.finapp.ui.feature.income.IncomeScreenUiState
import com.example.finapp.ui.feature.income.IncomeSumUiState
import com.example.finapp.ui.feature.income.asIncomeItemUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * ViewModel для экрана доходов.
 */
class IncomeViewModel(
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeScreenUiState())
    val uiState: StateFlow<IncomeScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val today = LocalDate.now().toString()

            transactionsRepository.getTransactions(startDate = today, endDate = today)
                .handleOutcome {
                    onSuccess {
                        val transactionsSum = data
                            .filter { transaction -> transaction.category.isIncome }
                            .sumOf { it.amount }
                            .stripTrailingZeros()
                            .toPlainString()
                        _uiState.update {
                            it.copy(
                                summary = IncomeSumUiState(totalFormatted = "$transactionsSum ₽"),
                                items = data
                                    .filter { transaction -> transaction.category.isIncome }
                                    .map { transaction -> transaction.asIncomeItemUiState() }
                                    .toImmutableList()
                            )
                        }
                    }
                }
        }
    }
}
