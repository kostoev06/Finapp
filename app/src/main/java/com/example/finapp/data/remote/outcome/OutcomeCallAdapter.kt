package com.example.finapp.data.remote.outcome

import com.example.finapp.data.common.Outcome
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class OutcomeCallAdapter(
    private val type: Type
) : CallAdapter<Type, Call<Outcome<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<Outcome<Type>> =
        OutcomeCall(call)
}