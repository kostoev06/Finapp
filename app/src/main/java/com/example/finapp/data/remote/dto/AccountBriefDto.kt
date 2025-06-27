package com.example.finapp.data.remote.dto

/**
 * DTO для краткой информации об аккаунте, получаемой из API.
 */
data class AccountBriefDto(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)
