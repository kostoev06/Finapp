package com.example.finapp.data.repository.impl

import com.example.finapp.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object CurrencyRepositoryImpl: CurrencyRepository {
    private val _currency = MutableStateFlow<String>("RUB")
    override val currency: StateFlow<String> = _currency

    override fun setCurrency(currency: String) {
        _currency.value = currency
    }
}