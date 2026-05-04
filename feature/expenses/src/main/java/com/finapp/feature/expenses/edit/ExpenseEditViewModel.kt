package com.finapp.feature.expenses.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.domain.usecase.GetAccountUseCase
import com.finapp.core.domain.usecase.GetCategoriesByTypeUseCase
import com.finapp.core.domain.usecase.GetTransactionByIdUseCase
import com.finapp.core.domain.usecase.SaveTransactionUseCase
import com.finapp.feature.common.R
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.text.UiText
import com.finapp.feature.common.utils.toErrorTexts
import com.finapp.feature.expenses.navigation.ExpensesNavigationDestination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.persistentListOf
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


sealed class ExpenseEditUiEvent {
    data class ShowError(val title: UiText, val message: UiText) : ExpenseEditUiEvent()
    data object OnSaveSuccess : ExpenseEditUiEvent()
}

class ExpenseEditViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val getAccount: GetAccountUseCase,
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val saveTransaction: SaveTransactionUseCase
) : ViewModel() {
    private val expenseId =
        savedStateHandle.toRoute<ExpensesNavigationDestination.EditExpense>().expenseId

    private val _uiState = MutableStateFlow(
        ExpenseEditScreenUiState(
            expenseId = expenseId,
            accountFieldState = "",
            currentCategoryState = CategoryUiState(0, ""),
            categoriesListState = persistentListOf(),
            isCategoryDialogVisible = false,
            amountFieldState = "",
            dateState = LocalDate.now(),
            timeState = LocalTime.now(),
            commentFieldState = ""
        )
    )
    val uiState: StateFlow<ExpenseEditScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ExpenseEditUiEvent>()
    val events: SharedFlow<ExpenseEditUiEvent> = _events.asSharedFlow()

    init {
        if (expenseId != null) loadExisting(expenseId)
        loadAccountAndCategories()
    }

    private fun loadExisting(id: Long) {
        viewModelScope.launch {
            val result = getTransactionById(id)
            result.data?.let { applyTransaction(it) }
            result.error?.let { failure ->
                val (title, message) = failure.toErrorTexts()
                _events.emit(ExpenseEditUiEvent.ShowError(title, message))
            }
        }
    }

    private fun loadAccountAndCategories() {
        viewModelScope.launch {
            val accountResult = getAccount()
            val accountName = accountResult.data?.name.orEmpty()
            if (accountResult.error != null) {
                _events.emit(
                    ExpenseEditUiEvent.ShowError(
                        title = UiText.Resource(R.string.error),
                        message = UiText.Resource(R.string.error_unknown)
                    )
                )
            }
            val categoriesResult = getCategoriesByType(isIncome = false)
            _uiState.update {
                it.copy(
                    accountFieldState = accountName,
                    categoriesListState = categoriesResult.data
                        .map { category -> category.asCategoryUiState() }
                        .toImmutableList()
                )
            }
        }
    }

    fun onAmountChange(new: String) {
        _uiState.update { it.copy(amountFieldState = new) }
    }

    fun onCategoryClick() {
        _uiState.update { it.copy(isCategoryDialogVisible = true) }
    }

    fun onCategorySelect(categoryId: Long) {
        _uiState.update { state ->
            val picked = state.categoriesListState.firstOrNull { it.id == categoryId }
                ?: return@update state
            state.copy(
                currentCategoryState = picked,
                isCategoryDialogVisible = false
            )
        }
    }

    fun onCancelDialog() {
        _uiState.update { it.copy(isCategoryDialogVisible = false) }
    }

    fun onChooseDate(date: LocalDate) {
        _uiState.update { it.copy(dateState = date) }
    }

    fun onChooseTime(time: LocalTime) {
        _uiState.update { it.copy(timeState = time) }
    }

    fun onCommentChange(new: String) {
        _uiState.update { it.copy(commentFieldState = new) }
    }

    fun onSave() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.amountFieldState.isEmpty()) {
                _events.emit(
                    ExpenseEditUiEvent.ShowError(
                        title = UiText.Resource(R.string.error),
                        message = UiText.Resource(R.string.error_invalid_amount)
                    )
                )
                return@launch
            }
            val brief = TransactionBrief(
                id = expenseId,
                categoryId = state.currentCategoryState.id,
                amount = state.amountFieldState.toBigDecimal(),
                transactionDate = LocalDateTime.of(state.dateState, state.timeState),
                comment = state.commentFieldState
            )
            when (val result = saveTransaction(brief)) {
                is SaveTransactionUseCase.Result.Synced -> {
                    _events.emit(ExpenseEditUiEvent.OnSaveSuccess)
                }
                is SaveTransactionUseCase.Result.Offline -> {
                    _events.emit(ExpenseEditUiEvent.OnSaveSuccess)
                    val (title, message) = result.error.toErrorTexts()
                    _events.emit(ExpenseEditUiEvent.ShowError(title, message))
                }
            }
        }
    }

    private fun applyTransaction(data: Transaction) {
        _uiState.update {
            it.copy(
                currentCategoryState = data.category.asCategoryUiState(),
                amountFieldState = data.amount.toString(),
                dateState = data.updatedAt.toLocalDate(),
                timeState = data.updatedAt.toLocalTime(),
                commentFieldState = data.comment ?: ""
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<ExpenseEditViewModel>
}
