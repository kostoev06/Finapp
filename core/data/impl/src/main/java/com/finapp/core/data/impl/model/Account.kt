package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.remote.api.model.AccountDto
import com.finapp.core.remote.api.model.AccountResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun AccountDto.asAccount(): Account =
    Account(
        id = id,
        name = name,
        balance = balance.toBigDecimal(),
        currency = CurrencyCode.from(currency),
        createdAt = LocalDateTime.parse(
            createdAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            updatedAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )

fun AccountResponse.asAccount(): Account =
    Account(
        id = id,
        name = name,
        balance = balance.toBigDecimal(),
        currency = CurrencyCode.from(currency),
        createdAt = LocalDateTime.parse(
            createdAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            updatedAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )