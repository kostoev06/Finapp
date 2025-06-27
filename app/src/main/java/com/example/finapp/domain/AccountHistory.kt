package com.example.finapp.domain

import java.time.LocalDateTime
import java.time.OffsetDateTime

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
