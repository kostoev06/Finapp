package com.finapp.feature.common.sound

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.staticCompositionLocalOf
import com.finapp.core.settings.api.repository.SoundSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/** Виды короткой звуковой обратной связи UI. */
enum class SoundEffect { Success, Error }

/**
 * Проигрывает короткие звуковые эффекты на действия в UI.
 *
 * Реализация через [ToneGenerator] — никаких бинарных ассетов в репе, всё генерируется ОС;
 * звучит «системно», но достаточно для UX-фидбека (success — короткое подтверждение, error —
 * низкий «отказ»). Заменить на `SoundPool` + `.ogg` можно без правок интерфейса.
 */
interface SoundPlayer {
    /**
     * @param ignoreSetting `true` — играть даже если в настройках звук выключен. Используется
     *   только для тестовых кнопок на экране Звуков, чтобы пользователь мог послушать перед
     *   включением.
     */
    fun play(effect: SoundEffect, ignoreSetting: Boolean = false)
}

@Singleton
class SoundPlayerImpl @Inject constructor(
    soundSettingsRepository: SoundSettingsRepository
) : SoundPlayer {

    // Первое значение читаем синхронно — иначе race с самым ранним play() (DataStore эмитит
    // быстро, но не мгновенно). Дальше держим up-to-date snapshot через collect.
    @Volatile
    private var enabled: Boolean = runBlocking { soundSettingsRepository.enabled.first() }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        soundSettingsRepository.enabled
            .onEach { enabled = it }
            .launchIn(scope)
    }

    // ToneGenerator держит ресурс — создаём один раз, lazily. NOTIFICATION-стрим подхватывает
    // соответствующую системную громкость пользователя.
    private val tone by lazy {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, VOLUME_PERCENT)
    }

    override fun play(effect: SoundEffect, ignoreSetting: Boolean) {
        if (!ignoreSetting && !enabled) return
        val (toneType, durationMs) = when (effect) {
            SoundEffect.Success -> ToneGenerator.TONE_PROP_ACK to 200
            SoundEffect.Error -> ToneGenerator.TONE_PROP_NACK to 300
        }
        tone.startTone(toneType, durationMs)
    }

    private companion object {
        const val VOLUME_PERCENT = 80
    }
}

val LocalSoundPlayer = staticCompositionLocalOf<SoundPlayer> {
    error("SoundPlayer not provided")
}
