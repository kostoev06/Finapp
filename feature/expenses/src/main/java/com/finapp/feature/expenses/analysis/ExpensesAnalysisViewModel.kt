package com.finapp.feature.expenses.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.domain.usecase.GetTransactionsByPeriodUseCase
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.text.UiText
import com.finapp.feature.common.utils.toErrorTexts
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class ExpensesAnalysisUiEvent {
    data class ShowError(val title: UiText, val message: UiText) : ExpensesAnalysisUiEvent()
}

/**
 * ViewModel для экрана анализа расходов.
 */
class ExpensesAnalysisViewModel @AssistedInject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase,
    currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesAnalysisScreenUiState())
    val uiState: StateFlow<ExpensesAnalysisScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ExpensesAnalysisUiEvent>()
    val events: SharedFlow<ExpensesAnalysisUiEvent> = _events.asSharedFlow()

    init {
        currencyRepository.currency
            .onEach { code -> _uiState.update { it.copy(currency = CurrencyCode.from(code)) } }
            .launchIn(viewModelScope)
        reload()
    }

    fun onChooseStartDate(date: LocalDate) {
        _uiState.update { it.copy(isLoading = true, startDate = date) }
        reload()
    }

    fun onChooseEndDate(date: LocalDate) {
        _uiState.update { it.copy(isLoading = true, endDate = date) }
        reload()
    }

    private fun reload() {
        viewModelScope.launch {
            val state = _uiState.value
            val result = getTransactionsByPeriod(start = state.startDate, end = state.endDate)
            updateUiState(result.data)
            result.error?.let { failure ->
                val (title, message) = failure.toErrorTexts()
                _events.emit(ExpensesAnalysisUiEvent.ShowError(title, message))
            }
        }
    }

    private fun updateUiState(data: List<Transaction>) {
        val onlyExpenses = data.filter { !it.category.isIncome }
        val total = onlyExpenses.sumOf { it.amount }.toFormattedString()
        val grouped = onlyExpenses.toAnalysisItems().toImmutableList()
        _uiState.update {
            it.copy(
                isLoading = false,
                summary = ExpensesAnalysisSumUiState(totalAmount = total),
                items = grouped
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpensesAnalysisViewModel>
}
