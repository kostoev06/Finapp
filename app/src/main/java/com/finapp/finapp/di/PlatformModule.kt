package com.finapp.finapp.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Биндинги системных Android-сервисов, которые зависят от `Context`.
 *
 * Цель — не таскать `Context` в репозитории/сервисы, а получать уже готовый системный объект
 * через DI. Если на устройстве сервиса нет (например, нет вибромотора) — провайд вернёт `null`,
 * потребитель сам обрабатывает этот случай.
 */
@Module
object PlatformModule {
    @Provides
    @Singleton
    fun provideVibrator(context: Context): Vibrator? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                ?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
}
