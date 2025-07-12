package com.finapp.core.remote.impl.outcome

import com.finapp.core.common.outcome.Outcome
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Адаптер для Retrofit, превращающий Call<T> в OutcomeCall<T>.
 */
internal class OutcomeCallAdapter(
    private val type: Type
) : CallAdapter<Type, Call<Outcome<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<Outcome<Type>> =
        OutcomeCall(call)
}