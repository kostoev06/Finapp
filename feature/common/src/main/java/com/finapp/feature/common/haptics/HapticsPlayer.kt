package com.finapp.feature.common.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.staticCompositionLocalOf
import com.finapp.core.settings.api.repository.HapticsSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/** Виды короткой тактильной обратной связи UI. */
enum class HapticEffect { Success, Error }

/**
 * Проигрывает короткую вибрацию на действия в UI.
 *
 * Реализация через системный [Vibrator] + [VibrationEffect.createPredefined] — никаких
 * паттернов в коде, всё описано ОС: success — короткий клик, error — двойной клик.
 * На устройствах без вибромотора (`hasVibrator() == false`) тихо ничего не делает.
 */
interface HapticsPlayer {
    /**
     * @param ignoreSetting `true` — играть даже если в настройках хаптики выключены.
     *   Используется только для тестовых кнопок на экране Хаптик.
     */
    fun play(effect: HapticEffect, ignoreSetting: Boolean = false)
}

@Singleton
class HapticsPlayerImpl @Inject constructor(
    private val context: Context,
    hapticsSettingsRepository: HapticsSettingsRepository
) : HapticsPlayer {

    @Volatile
    private var enabled: Boolean = runBlocking { hapticsSettingsRepository.enabled.first() }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        hapticsSettingsRepository.enabled
            .onEach { enabled = it }
            .launchIn(scope)
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)
                ?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    override fun play(effect: HapticEffect, ignoreSetting: Boolean) {
        if (!ignoreSetting && !enabled) return
        val v = vibrator?.takeIf { it.hasVibrator() } ?: return
        v.vibrate(buildEffect(effect))
    }

    /**
     * `createPredefined` появился на API 29 — даёт «нативные» паттерны от ОС/прошивки.
     * На API 26–28 эмулируем теми же длительностями через `createWaveform`.
     */
    private fun buildEffect(effect: HapticEffect): VibrationEffect =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val id = when (effect) {
                HapticEffect.Success -> VibrationEffect.EFFECT_CLICK
                HapticEffect.Error -> VibrationEffect.EFFECT_DOUBLE_CLICK
            }
            VibrationEffect.createPredefined(id)
        } else {
            val pattern = when (effect) {
                HapticEffect.Success -> longArrayOf(0L, 30L)
                HapticEffect.Error -> longArrayOf(0L, 50L, 80L, 50L)
            }
            VibrationEffect.createWaveform(pattern, /* repeat = */ -1)
        }
}

val LocalHapticsPlayer = staticCompositionLocalOf<HapticsPlayer> {
    error("HapticsPlayer not provided")
}
