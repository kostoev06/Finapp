package com.finapp.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.feature.account.AccountViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class HomeViewModel @AssistedInject constructor(
    private val accountRepository: AccountRepository,
    private val currencyRepository: CurrencyRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        viewModelScope.launch {
            accountRepository.getAccount().handleOutcome {
                onSuccess {
                    currencyRepository.setCurrency(data.currency.code)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<HomeViewModel>
}