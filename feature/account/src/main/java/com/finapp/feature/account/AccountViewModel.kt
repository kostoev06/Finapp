package com.finapp.feature.account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.feature.account.di.AccountViewModelsModule
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.utils.toFormattedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class AccountUiEvent {
    data class ShowError(val title: String, val message: String) : AccountUiEvent()
}

/**
 * ViewModel для экрана счета.
 */
class AccountViewModel @AssistedInject constructor(
    private val accountRepository: AccountRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountScreenUiState())
    val uiState: StateFlow<AccountScreenUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AccountUiEvent>()
    val events: SharedFlow<AccountUiEvent> = _events.asSharedFlow()

    init {
        loadAccount()
    }

    fun loadAccount() {
        viewModelScope.launch {
            accountRepository.getAccount()
                .handleOutcome {
                    onSuccess {
                        val balance = data.balance.toFormattedString()
                        _uiState.update {
                            it.copy(
                                balanceState = BalanceItemUiState(balance),
                                currencyState = CurrencyItemUiState(data.currency)
                            )
                        }
                    }
                    onFailure {
                        onError {
                            _events.emit(
                                AccountUiEvent.ShowError(
                                    title = "Ошибка $code",
                                    message = errorBody ?: "Неизвестная ошибка"
                                )
                            )
                        }
                        onException {
                            _events.emit(
                                AccountUiEvent.ShowError(
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
    interface Factory : ViewModelAssistedFactory<AccountViewModel>
}
