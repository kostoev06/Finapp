package com.finapp.core.data.api.model

enum class CurrencyCode(val code: String) {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    companion object {
        fun from(code: String): CurrencyCode = entries
            .find { it.code.equals(code, ignoreCase = true) }
            ?: throw IllegalArgumentException("Unknown currency code: $code")
    }
}