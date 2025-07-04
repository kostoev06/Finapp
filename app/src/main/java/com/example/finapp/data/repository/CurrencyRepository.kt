package com.example.finapp.data.repository

import kotlinx.coroutines.flow.StateFlow

interface CurrencyRepository {
    val currency: StateFlow<String>
    fun setCurrency(currency: String)
}