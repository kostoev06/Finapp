package com.finapp.core.work.transaction.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface WorkerAssistedFactory<LW: ListenableWorker> {
    fun create(context: Context, workerParameters: WorkerParameters): LW
}