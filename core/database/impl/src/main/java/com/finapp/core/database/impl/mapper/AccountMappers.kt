package com.finapp.core.database.impl.mapper

import com.finapp.core.database.api.entity.AccountEntity
import com.finapp.core.database.impl.entity.AccountRoomEntity
import java.time.format.DateTimeFormatter

private val iso = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun AccountRoomEntity.toEntity(): AccountEntity =
    AccountEntity(
        id = id,
        name = name,
        balance = balance.toBigDecimal(),
        currency = currency
    )

fun AccountEntity.toRoom(): AccountRoomEntity =
    AccountRoomEntity(
        id = id,
        name = name,
        balance = balance.stripTrailingZeros().toPlainString(),
        currency = currency
    )
