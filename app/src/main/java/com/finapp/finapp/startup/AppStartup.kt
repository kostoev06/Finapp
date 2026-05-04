package com.finapp.finapp.startup

import com.finapp.core.common.outcome.handleOutcome
import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.work.transaction.ConnectivityObserver
import com.finapp.core.work.transaction.SyncTransactionScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Бутстрап на старте приложения: триггер синхронизации при появлении сети + прогрев
 * локального кэша аккаунта и категорий.
 *
 * Раньше всё это жило в `HomeViewModel.init`, что было неправильно по двум причинам:
 *   1. Это app-level concern, а не home-screen concern: например, прогрев категорий нужен
 *      и на экране редактирования транзакции, который доступен ещё до показа Home.
 *   2. VM пересоздаётся при каждом билде subcomponent (т.е. каждом входе на Home), и
 *      повторный прогрев на горячем старте — лишний.
 *
 * [run] вызывается ровно один раз из [com.finapp.finapp.FinappApplication.onCreate].
 */
@Singleton
class AppStartup @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val currencyRepository: CurrencyRepository,
    private val connectivity: ConnectivityObserver,
    private val scheduler: SyncTransactionScheduler
) {
    // Application-уровневая корутина: переживает любые UI-сцены, отменяется только при
    // завершении процесса. SupervisorJob — чтобы упавший прогрев не убил подписку на сеть.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun run() {
        connectivity.online
            .filter { it }
            .onEach { scheduler.scheduleOneShot() }
            .launchIn(scope)

        scope.launch {
            accountRepository.fetchAccount().handleOutcome {
                onSuccess {
                    accountRepository.insertLocalAccount(data)
                    currencyRepository.setCurrency(data.currency.code)
                }
            }
        }

        scope.launch {
            categoryRepository.fetchCategories().handleOutcome {
                onSuccess { categoryRepository.insertAllLocalCategories(data) }
            }
        }
    }
}
