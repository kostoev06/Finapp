package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Account
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.core.database.api.entity.AccountEntity
import com.finapp.core.remote.api.model.AccountDto
import com.finapp.core.remote.api.model.AccountResponse
import com.finapp.core.remote.api.model.AccountUpdateRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun AccountDto.asAccount(): Account =
    Account(
        id = id,
        name = name,
        balance = balance.toBigDecimal(),
        currency = CurrencyCode.from(currency)
    )

fun AccountResponse.asAccount(): Account =
    Account(
        id = id,
        name = name,
        balance = balance.toBigDecimal(),
        currency = CurrencyCode.from(currency)
    )

fun AccountEntity.asAccount(): Account =
    Account(
        id = id,
        name = name,
        balance = balance,
        currency = CurrencyCode.from(currency)
    )

fun Account.asAccountUpdateRequest(): AccountUpdateRequest =
    AccountUpdateRequest(
        name = name,
        balance = balance.stripTrailingZeros().toPlainString(),
        currency = currency.code
    )

fun Account.asAccountEntity(): AccountEntity =
    AccountEntity(
        id = id,
        name = name,
        balance = balance,
        currency = currency.code
    )