package com.finapp.core.database.api.entity

import java.math.BigDecimal

data class AccountEntity(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val isSynced: Boolean
)
