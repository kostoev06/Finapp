package com.finapp.core.data.api.repository

import kotlinx.coroutines.flow.Flow

/**
 * Текущая валюта аккаунта.
 *
 * Хранится отдельно от модели [com.finapp.core.data.api.model.Account], чтобы экраны,
 * которым нужен только знак валюты, не подписывались на весь account-стейт. Значение
 * персистится — иначе при холодном старте без сети все суммы рисовались бы в дефолтной
 * валюте, пока бэкенд не отдаст аккаунт.
 */
interface CurrencyRepository {
    val currency: Flow<String>
    suspend fun setCurrency(currency: String)
}
