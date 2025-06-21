package com.example.finapp.data.remote.outcome

import com.example.finapp.data.common.Outcome
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutcomeCall<T>(
    private val proxy: Call<T>
) : Call<Outcome<T>> {

    override fun enqueue(callback: Callback<Outcome<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val remoteResult = response.asRemoteResult()
                callback.onResponse(this@OutcomeCall, Response.success(remoteResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val remoteResult = Outcome.Exception(t)
                callback.onResponse(this@OutcomeCall, Response.success(remoteResult))
            }
        })
    }

    override fun execute(): Response<Outcome<T>> = throw NotImplementedError()
    override fun clone(): Call<Outcome<T>> = OutcomeCall(proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() { proxy.cancel() }
}