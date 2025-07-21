package com.finapp.core.work.transaction.di

import androidx.lifecycle.ViewModel
import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class WorkerKey(val value: KClass<out ListenableWorker>)