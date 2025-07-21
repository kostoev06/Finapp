package com.finapp.core.work.transaction.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.WorkManager
import com.finapp.core.work.transaction.ConnectivityObserver
import com.finapp.core.work.transaction.SyncTransactionScheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WorkSupportModule {
    @Provides
    @Singleton
    fun provideConnectivityManager(app: Context): ConnectivityManager =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        connectivityManager: ConnectivityManager
    ): ConnectivityObserver = ConnectivityObserver(connectivityManager)

    @Provides
    @Singleton
    fun provideWorkManager(app: Context): WorkManager =
        WorkManager.getInstance(app)

    @Provides
    @Singleton
    fun provideSyncScheduler(
        workManager: WorkManager
    ): SyncTransactionScheduler = SyncTransactionScheduler(workManager)
}