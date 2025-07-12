package com.finapp.core.remote.impl.outcome

import com.finapp.core.common.outcome.Outcome
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Factory для OutcomeCallAdapter (пока не используется).
 */
internal class OutcomeCallAdapterFactory private constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != Outcome::class.java) {
            return null
        }

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return OutcomeCallAdapter(resultType)
    }

    companion object {
        fun create(): OutcomeCallAdapterFactory = OutcomeCallAdapterFactory()
    }
}
