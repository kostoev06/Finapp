package com.finapp.core.work.transaction

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.finapp.core.work.transaction.di.FinappWorkComponent

class DefaultWorkerFactory(
    private val workComponentBuilder: FinappWorkComponent.Builder
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val component = workComponentBuilder
            .build()
        val workersMap = component.workerFactory()
        return workersMap[Class.forName(workerClassName)]?.create(appContext, workerParameters)
    }
}