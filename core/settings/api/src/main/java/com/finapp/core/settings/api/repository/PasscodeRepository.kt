package com.finapp.core.settings.api.repository

import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий код-пароля.
 *
 * Сам код в открытом виде нигде не хранится: сохраняется только PBKDF2-хеш и соль.
 */
interface PasscodeRepository {
    /** Установлен ли код-пароль. */
    val isSet: Flow<Boolean>

    /** Установить новый код. Перезаписывает предыдущий, если был. */
    suspend fun set(plain: String)

    /** Сравнить введённый код с сохранённым. Возвращает false, если код не установлен. */
    suspend fun verify(plain: String): Boolean

    /** Удалить сохранённый код. */
    suspend fun clear()
}
