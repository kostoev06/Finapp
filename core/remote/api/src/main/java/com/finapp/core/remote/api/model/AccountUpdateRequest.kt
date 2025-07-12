package com.finapp.core.remote.api.model

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)
