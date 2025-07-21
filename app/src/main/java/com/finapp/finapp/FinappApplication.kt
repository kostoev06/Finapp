package com.finapp.finapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.finapp.finapp.di.AppComponent
import com.finapp.finapp.di.DaggerAppComponent
import com.finapp.core.work.transaction.DefaultWorkerFactory
import com.finapp.core.work.transaction.SyncTransactionScheduler
import javax.inject.Inject

class FinappApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent
    @Inject lateinit var scheduler: SyncTransactionScheduler

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).application(this).build()
        appComponent.inject(this)
        scheduler.schedulePeriodic()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(DefaultWorkerFactory(appComponent.workerComponentBuilder()))
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}