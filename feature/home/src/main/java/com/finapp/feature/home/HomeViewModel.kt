package com.finapp.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.work.transaction.ConnectivityObserver
import com.finapp.core.work.transaction.SyncTransactionScheduler
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel @AssistedInject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val connectivity: ConnectivityObserver,
    private val scheduler: SyncTransactionScheduler,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        // Подписка на сеть и прогрев кеша живут независимо: collect на connectivity никогда
        // не возвращается, поэтому fetch'и нельзя ставить в тот же launch — они не выполнятся.
        connectivity.online
            .filter { it }
            .onEach { scheduler.scheduleOneShot() }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            accountRepository.fetchAccount().handleOutcome {
                onSuccess {
                    accountRepository.insertLocalAccount(data)
                    currencyRepository.setCurrency(data.currency.code)
                }
            }
        }

        viewModelScope.launch {
            categoryRepository.fetchCategories().handleOutcome {
                onSuccess {
                    categoryRepository.insertAllLocalCategories(data)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<HomeViewModel>
}