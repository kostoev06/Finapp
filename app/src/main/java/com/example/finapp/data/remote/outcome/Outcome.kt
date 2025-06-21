package com.example.finapp.data.remote.outcome

import com.example.finapp.data.common.Outcome
import retrofit2.Response

fun <T> Response<T>.asRemoteResult(): Outcome<T> {
    val body = body()
    return if (isSuccessful && body != null) {
        Outcome.Success(body)
    } else {
        Outcome.Error(code())
    }
}