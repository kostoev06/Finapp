package com.finapp.feature.account.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.repository.AccountIdProvider
import com.finapp.core.domain.usecase.GetAccountUseCase
import com.finapp.core.domain.usecase.UpdateAccountUseCase
import com.finapp.feature.account.homepage.BalanceItemUiState
import com.finapp.feature.account.homepage.CurrencyItemUiState
import com.finapp.feature.common.R
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.text.UiText
import com.finapp.feature.common.utils.toErrorTexts
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AccountEditUiEvent {
    data class ShowError(val title: UiText, val message: UiText) : AccountEditUiEvent()
    data object OnSaveSuccess : AccountEditUiEvent()
}

/**
 * ViewModel для экрана изменения счета.
 */
class AccountEditViewModel @AssistedInject constructor(
    private val getAccount: GetAccountUseCase,
    private val updateAccount: UpdateAccountUseCase,
    private val accountIdProvider: AccountIdProvider,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountEditScreenUiState())
    val uiState: StateFlow<AccountEditScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountEditUiEvent>()
    val events: SharedFlow<AccountEditUiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = getAccount()
            result.data?.let { updateUiState(it) }
            result.error?.let { failure ->
                val (title, message) = failure.toErrorTexts()
                _events.emit(AccountEditUiEvent.ShowError(title, message))
            }
        }
    }

    fun onNameChange(new: String) {
        _uiState.update { it.copy(nameFieldState = it.nameFieldState.copy(text = new)) }
    }

    fun onBalanceChange(new: String) {
        _uiState.update { it.copy(balanceFieldState = it.balanceFieldState.copy(text = new)) }
    }

    fun onCurrencyClick() {
        _uiState.update { it.copy(isCurrencySheetVisible = true) }
    }

    fun onCurrencySelect(currency: CurrencyCode) {
        _uiState.update {
            it.copy(
                currencyFieldState = it.currencyFieldState.copy(currency = currency),
                isCurrencySheetVisible = false
            )
        }
    }

    fun onCancelSheet() {
        _uiState.update { it.copy(isCurrencySheetVisible = false) }
    }

    fun onSave() {
        viewModelScope.launch {
            if (_uiState.value.balanceFieldState.text.isEmpty()) {
                _events.emit(
                    AccountEditUiEvent.ShowError(
                        title = UiText.Resource(R.string.error),
                        message = UiText.Resource(R.string.error_invalid_amount)
                    )
                )
                return@launch
            }
            val state = _uiState.value
            updateAccount(
                Account(
                    id = accountIdProvider.get(),
                    name = state.nameFieldState.text,
                    balance = state.balanceFieldState.text.toBigDecimal(),
                    currency = state.currencyFieldState.currency
                )
            ).handleOutcome {
                onSuccess {
                    _events.emit(AccountEditUiEvent.OnSaveSuccess)
                }
                onFailure {
                    val (title, message) = outcome.toErrorTexts()
                    _events.emit(AccountEditUiEvent.ShowError(title, message))
                }
            }
        }
    }

    private fun updateUiState(data: Account) {
        _uiState.update {
            it.copy(
                nameFieldState = NameFieldUiState(data.name),
                balanceFieldState = BalanceFieldUiState(data.balance.toPlainString()),
                currencyFieldState = CurrencyFieldUiState(data.currency)
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AccountEditViewModel>
}
