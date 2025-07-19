package com.finapp.feature.account.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.feature.common.di.ViewModelAssistedFactory
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
    data class ShowError(val title: String, val message: String) : AccountEditUiEvent()
    data object OnSaveSuccess : AccountEditUiEvent()
}

/**
 * ViewModel для экрана изменения счета.
 */
class AccountEditViewModel @AssistedInject constructor(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountEditScreenUiState())
    val uiState: StateFlow<AccountEditScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountEditUiEvent>()
    val events: SharedFlow<AccountEditUiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            accountRepository.fetchAccount()
                .handleOutcome {
                    onSuccess {
                        val balance = data.balance.toPlainString()
                        _uiState.update {
                            it.copy(
                                nameFieldState = NameFieldUiState(data.name),
                                balanceFieldState = BalanceFieldUiState(balance),
                                currencyFieldState = CurrencyFieldUiState(data.currency)
                            )
                        }
                    }
                    onFailure {
                        onError {
                            _events.emit(
                                AccountEditUiEvent.ShowError(
                                    title = "Ошибка $code",
                                    message = errorBody ?: "Неизвестная ошибка"
                                )
                            )
                        }
                        onException {
                            _events.emit(
                                AccountEditUiEvent.ShowError(
                                    title = "Ошибка",
                                    message = "Неизвестная ошибка"
                                )
                            )
                        }
                    }
                }
        }
    }

    fun onNameChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(nameFieldState = it.nameFieldState.copy(text = new))
            }
        }
    }

    fun onBalanceChange(new: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(balanceFieldState = it.balanceFieldState.copy(text = new))
            }
        }
    }

    fun onCurrencyClick() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCurrencySheetVisible = true)
            }
        }
    }

    fun onCurrencySelect(currency: CurrencyCode) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    currencyFieldState = it.currencyFieldState.copy(currency = currency),
                    isCurrencySheetVisible = false
                )
            }
        }
    }

    fun onCancelSheet() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isCurrencySheetVisible = false)
            }
        }
    }

    fun onSave() {
        viewModelScope.launch {
            accountRepository.updateAccount(
                _uiState.value.nameFieldState.text,
                _uiState.value.balanceFieldState.text,
                _uiState.value.currencyFieldState.currency.code
            ).handleOutcome {
                onSuccess {
                    _events.emit(AccountEditUiEvent.OnSaveSuccess)
                    currencyRepository.setCurrency(_uiState.value.currencyFieldState.currency.code)
                }
                onFailure {
                    onError {
                        _events.emit(
                            AccountEditUiEvent.ShowError(
                                title = "Ошибка $code",
                                message = errorBody ?: "Неизвестная ошибка"
                            )
                        )
                    }
                    onException {
                        _events.emit(
                            AccountEditUiEvent.ShowError(
                                title = "Ошибка",
                                message = "Неизвестная ошибка"
                            )
                        )
                    }
                }

            }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AccountEditViewModel>
}