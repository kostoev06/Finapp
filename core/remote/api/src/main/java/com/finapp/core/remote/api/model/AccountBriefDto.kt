package com.finapp.core.remote.api.model

/**
 * DTO для краткой информации об аккаунте, получаемой из API.
 */
data class AccountBriefDto(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)
