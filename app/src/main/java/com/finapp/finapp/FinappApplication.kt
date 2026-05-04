package com.finapp.finapp

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.work.Configuration
import com.finapp.core.settings.api.repository.LanguageRepository
import com.finapp.finapp.di.AppComponent
import com.finapp.finapp.di.DaggerAppComponent
import com.finapp.core.work.transaction.DefaultWorkerFactory
import com.finapp.core.work.transaction.SyncTransactionScheduler
import com.finapp.finapp.startup.AppStartup
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FinappApplication : Application(), Configuration.Provider {
    lateinit var appComponent: AppComponent
    @Inject lateinit var scheduler: SyncTransactionScheduler
    @Inject lateinit var languageRepository: LanguageRepository
    @Inject lateinit var appStartup: AppStartup

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).application(this).build()
        appComponent.inject(this)
        applySavedLocale()
        scheduler.schedulePeriodic()
        appStartup.run()
    }

    /**
     * Сохранённый язык применяем синхронно до первой отрисовки Activity, иначе UI мигнёт
     * на дефолтной локали. Чтение DataStore однократное и быстрое — runBlocking здесь оправдан.
     */
    private fun applySavedLocale() {
        val option = runBlocking { languageRepository.language.first() }
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(option.tag)
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(DefaultWorkerFactory(appComponent.workerComponentBuilder()))
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}
