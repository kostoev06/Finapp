package com.finapp.core.data.impl.model

import com.finapp.core.data.api.model.Transaction
import com.finapp.core.data.api.model.TransactionBrief
import com.finapp.core.data.api.model.TransactionInfo
import com.finapp.core.database.api.entity.TransactionAndCategory
import com.finapp.core.database.api.entity.TransactionEntity
import com.finapp.core.remote.api.model.TransactionDto
import com.finapp.core.remote.api.model.TransactionRequest
import com.finapp.core.remote.api.model.TransactionResponse
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun TransactionDto.asTransactionInfo(): TransactionInfo = TransactionInfo(
    id = id,
    accountId = accountId,
    categoryId = categoryId,
    amount = amount.toBigDecimal(),
    transactionDate = LocalDateTime.parse(
        transactionDate,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
    ),
    comment = comment,
    createdAt = LocalDateTime.parse(
        createdAt,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
    ),
    updatedAt = LocalDateTime.parse(
        updatedAt,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
    )
)

fun TransactionResponse.asTransaction(): Transaction =
    Transaction(
        id = id,
        accountId = account.id,
        category = category.asCategory(),
        amount = amount.toBigDecimal(),
        transactionDate = LocalDateTime.parse(
            transactionDate,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        comment = comment,
        createdAt = LocalDateTime.parse(
            createdAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ),
        updatedAt = LocalDateTime.parse(
            updatedAt,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        )
    )

fun TransactionAndCategory.asTransaction(): Transaction =
    Transaction(
        id = transaction.id,
        accountId = transaction.accountBackendId,
        category = category.asCategory(),
        amount = transaction.amount,
        transactionDate = transaction.transactionDate,
        comment = transaction.comment,
        createdAt = transaction.createdAt,
        updatedAt = transaction.updatedAt
    )

fun TransactionBrief.asTransactionRequest(accountId: Long): TransactionRequest =
    TransactionRequest(
        accountId = accountId,
        categoryId = categoryId,
        amount = amount.stripTrailingZeros().toPlainString(),
        transactionDate = transactionDate.atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        comment = comment
    )

fun Transaction.asTransactionEntity(
    isSynced: Boolean,
    isNew: Boolean
): TransactionEntity =
    TransactionEntity(
        id = id,
        accountBackendId = accountId,
        categoryBackendId = category.id,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isNew = isNew
    )

fun TransactionInfo.asTransactionEntity(
    isSynced: Boolean,
    isNew: Boolean
): TransactionEntity =
    TransactionEntity(
        id = id,
        accountBackendId = accountId,
        categoryBackendId = categoryId,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        isNew = isNew
    )
