package com.finapp.core.settings.api.repository

import kotlinx.coroutines.flow.Flow

/**
 * Глобальная настройка проигрывания звуковых эффектов в UI.
 */
interface SoundSettingsRepository {
    /** `true` — звуки на действия проигрываются. Дефолт для нового пользователя. */
    val enabled: Flow<Boolean>
    suspend fun setEnabled(enabled: Boolean)
}
