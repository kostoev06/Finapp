package com.finapp.core.work.transaction.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ListenableWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.finapp.core.work.transaction.SyncTransactionWorker
import dagger.Provides
import dagger.multibindings.Multibinds
import javax.inject.Provider

@Module
interface WorkersModule {
    @Binds
    @IntoMap
    @WorkerKey(SyncTransactionWorker::class)
    fun bindSyncTransactionWorker(
        factory: SyncTransactionWorker.Factory
    ): WorkerAssistedFactory<out ListenableWorker>
}