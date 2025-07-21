package com.finapp.core.work.transaction.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.Multibinds
import javax.inject.Provider

@Subcomponent(
    modules = [
        WorkersModule::class
    ]
)
interface FinappWorkComponent {
    fun workerFactory(): Map<Class<out ListenableWorker>, @JvmSuppressWildcards WorkerAssistedFactory<out ListenableWorker>>

    @Subcomponent.Builder
    interface Builder {
        fun build(): FinappWorkComponent
    }

    @Module(subcomponents = [FinappWorkComponent::class])
    interface InstallationModule
}
