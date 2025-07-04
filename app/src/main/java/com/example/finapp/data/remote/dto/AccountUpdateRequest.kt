package com.example.finapp.data.remote.dto

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)
