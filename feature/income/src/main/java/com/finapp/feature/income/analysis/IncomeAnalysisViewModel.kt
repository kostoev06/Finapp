package com.finapp.feature.income.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.asTransactionInfo
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

sealed class IncomeAnalysisUiEvent {
    data class ShowError(val title: String, val message: String) : IncomeAnalysisUiEvent()
}

/**
 * ViewModel для экрана анализа доходов.
 */
class IncomeAnalysisViewModel @AssistedInject constructor(
    private val transactionRepository: TransactionRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeAnalysisScreenUiState())
    val uiState: StateFlow<IncomeAnalysisScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<IncomeAnalysisUiEvent>()
    val events: SharedFlow<IncomeAnalysisUiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            currencyRepository.currency.collect { currency ->
                _uiState.update {
                    it.copy(
                        currency = CurrencyCode.from(currency)
                    )
                }
            }
        }
        loadIncomeAnalysis()
    }

    fun onChooseStartDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    startDate = date
                )
            }
            fetchCategoriesSums()
        }
    }

    fun onChooseEndDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    endDate = date
                )
            }
            fetchCategoriesSums()
        }
    }

    private fun loadIncomeAnalysis() {
        viewModelScope.launch {
            fetchCategoriesSums()
        }
    }

    private suspend fun fetchCategoriesSums() {
        transactionRepository
            .fetchTransactionsByPeriod(
                startDate = _uiState.value.startDate.toString(),
                endDate = _uiState.value.endDate.toString()
            )
            .handleOutcome {
                onSuccess {
                    updateUiState(data)
                    data.forEach { transactionRepository.insertSyncedLocalTransaction(it.asTransactionInfo()) }
                }
                onFailure {
                    val todayStart = _uiState.value.startDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val todayEnd = LocalDateTime
                        .of(_uiState.value.endDate, LocalTime.MAX)
                        .atOffset(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    updateUiState(transactionRepository.getLocalTransactionsByPeriod(startIso = todayStart, endIso = todayEnd))

                    onError {
                        _events.emit(
                            IncomeAnalysisUiEvent.ShowError(
                                title = "Ошибка $code",
                                message = errorBody ?: "Неизвестная ошибка"
                            )
                        )
                    }
                    onException {
                        _events.emit(
                            IncomeAnalysisUiEvent.ShowError(
                                title = "Ошибка",
                                message = "Неизвестная ошибка"
                            )
                        )
                    }
                }
            }
    }

    private fun updateUiState(data: List<Transaction>) {
        val onlyIncome = data.filter { it.category.isIncome }

        val total = onlyIncome
            .sumOf { it.amount }
            .toFormattedString()

        val grouped = onlyIncome
            .toAnalysisItems()
            .toImmutableList()

        _uiState.update {
            it.copy(
                isLoading = false,
                summary = IncomeAnalysisSumUiState(totalAmount = total),
                items = grouped
            )
        }
    }


    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<IncomeAnalysisViewModel>
}
