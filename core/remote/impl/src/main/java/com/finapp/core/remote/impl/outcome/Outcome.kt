package com.finapp.core.remote.impl.outcome

import com.finapp.core.common.outcome.Outcome
import retrofit2.Response

fun <T> Response<T>.asRemoteResult(): Outcome<T> {
    val body = body()
    return if (isSuccessful && body != null) {
        Outcome.Success(body)
    } else {
        val errorText = try {
            errorBody()?.string()
        } catch (e: Exception) {
            null
        }
        Outcome.Error(
            code = code(),
            errorBody = errorText
        )
    }
}