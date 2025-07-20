package com.finapp.core.database.impl.mapper

import com.finapp.core.database.api.entity.TransactionEntity
import com.finapp.core.database.impl.entity.TransactionRoomEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val iso = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun TransactionRoomEntity.toEntity(): TransactionEntity =
    TransactionEntity(
        id = id,
        accountBackendId = accountId,
        categoryBackendId = categoryId,
        amount = amount.toBigDecimal(),
        transactionDate = LocalDateTime.parse(transactionDateIso, iso),
        comment = comment,
        createdAt = LocalDateTime.parse(createdAtIso, iso),
        updatedAt = LocalDateTime.parse(updatedAtIso, iso),
        isSynced = isSynced,
        isNew = isNew
    )

fun TransactionEntity.toRoom(): TransactionRoomEntity =
    TransactionRoomEntity(
        id = id,
        accountId = accountBackendId,
        categoryId = categoryBackendId,
        amount = amount.stripTrailingZeros().toPlainString(),
        transactionDateIso = transactionDate.atOffset(ZoneOffset.UTC).format(iso),
        comment = comment,
        createdAtIso = createdAt.atOffset(ZoneOffset.UTC).format(iso),
        updatedAtIso = updatedAt.atOffset(ZoneOffset.UTC).format(iso),
        isSynced = isSynced,
        isNew = isNew
    )