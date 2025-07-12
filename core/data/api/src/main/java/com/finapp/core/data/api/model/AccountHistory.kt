package com.finapp.core.data.api.model

import java.time.LocalDateTime

/**
 * Доменная модель записи истории операций по аккаунту.
 */
data class AccountHistory(
    val id: Long,
    val accountId: Long,
    val changeType: ChangeType,
    val previousState: AccountState,
    val newState: AccountState,
    val changeTimestamp: LocalDateTime,
    val createdAt: LocalDateTime
)
