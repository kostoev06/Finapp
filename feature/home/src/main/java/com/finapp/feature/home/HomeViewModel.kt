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
        viewModelScope.launch {
            connectivity.online
                .filter { it }
                .collect {
                    scheduler.scheduleOneShot()
                }

            accountRepository.fetchAccount().handleOutcome {
                onSuccess {
                    accountRepository.insertLocalAccount(data)
                    currencyRepository.setCurrency(data.currency.code)
                }
            }

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