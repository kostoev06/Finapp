package com.finapp.core.remote.api.model

data class TransactionRequest(
    val accountId: Long,
    val categoryId: Long,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)
