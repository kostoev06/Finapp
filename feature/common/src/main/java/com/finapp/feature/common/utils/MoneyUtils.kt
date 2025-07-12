package com.finapp.feature.common.utils

import androidx.annotation.StringRes
import com.finapp.core.data.api.model.CurrencyCode
import com.finapp.feature.common.R
import java.math.BigDecimal

@StringRes
fun currencySymbolRes(currency: CurrencyCode): Int =
    when (currency) {
        CurrencyCode.RUB -> R.string.currency_symbol_rub
        CurrencyCode.USD -> R.string.currency_symbol_usd
        CurrencyCode.EUR -> R.string.currency_symbol_eur
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
