package com.example.finapp.ui.utils

import java.math.BigDecimal

fun currencySymbol(currencyCode: String): String =
    when (currencyCode) {
        "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        else -> ""
    }

fun BigDecimal.toFormattedString(): String {
    val plain = this.stripTrailingZeros().toPlainString()
    val parts = plain.split('.', limit = 2)
    val integerPart = parts[0]
    val fractionalPart = parts.getOrNull(1)

    val grouped = integerPart
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()

    return if (!fractionalPart.isNullOrEmpty()) {
        "$grouped.$fractionalPart"
    } else {
        grouped
    }
}