package com.example.finapp.data.common

sealed interface Outcome<out T> {
    data class Success<T>(val data: T) : Outcome<T>

    sealed interface FailureOutcome : Outcome<Nothing>
    data class Error(val code: Int) : FailureOutcome
    data class Exception(val exception: Throwable) : FailureOutcome
}

fun <T, V> Outcome<T>.transform(transform: (T) -> V): Outcome<V> =
    when (this) {
        is Outcome.Success -> Outcome.Success(transform(data))
        is Outcome.FailureOutcome -> this
    }

@OutcomeDsl
inline fun <T> Outcome<T>.handleOutcome(lambda: OutcomeHandlerScope<T>.() -> Unit) {
    OutcomeHandlerScope(this).lambda()
}

@DslMarker
annotation class OutcomeDsl

@OutcomeDsl
class OutcomeHandlerScope<T>(
    val outcome: Outcome<T>
) {
    inline fun onSuccess(lambda: Outcome.Success<T>.() -> Unit) {
        if (outcome is Outcome.Success) lambda(outcome)
    }

    inline fun onFailure(lambda: FailureOutcomeHandlerScope.() -> Unit) {
        if (outcome is Outcome.FailureOutcome) FailureOutcomeHandlerScope(outcome).lambda()
    }
}

@OutcomeDsl
class FailureOutcomeHandlerScope(
    val outcome: Outcome.FailureOutcome
) {
    inline fun onError(lambda: Outcome.Error.() -> Unit) {
        if (outcome is Outcome.Error) lambda(outcome)
    }

    inline fun onException(lambda: Outcome.Exception.() -> Unit) {
        if (outcome is Outcome.Exception) lambda(outcome)
    }
}
