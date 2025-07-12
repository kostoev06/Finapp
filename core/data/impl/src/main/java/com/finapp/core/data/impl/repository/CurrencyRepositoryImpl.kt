package com.finapp.core.data.impl.repository

import com.finapp.core.data.api.repository.CurrencyRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(): CurrencyRepository {
    private val _currency = MutableStateFlow<String>("RUB")
    override val currency: StateFlow<String> = _currency

    override fun setCurrency(currency: String) {
        _currency.value = currency
    }
}
