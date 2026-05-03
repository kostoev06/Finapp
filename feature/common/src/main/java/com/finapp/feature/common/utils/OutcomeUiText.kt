package com.finapp.feature.common.utils

import com.finapp.core.common.outcome.Outcome
import com.finapp.feature.common.R
import com.finapp.feature.common.text.UiText

/**
 * Маппит ошибочный исход сети в пару (заголовок, сообщение) для показа пользователю.
 *
 * - [Outcome.Error] — HTTP-ответ с кодом: заголовок «Ошибка <code>», сообщение — тело ответа
 *   как есть (часто это технический текст с бэкенда; локализовать его на клиенте нельзя),
 *   а если тело пустое — общий «Неизвестная ошибка».
 * - [Outcome.Exception] — сетевое/системное исключение: общий «Ошибка / Неизвестная ошибка».
 */
fun Outcome.FailureOutcome.toErrorTexts(): Pair<UiText, UiText> = when (this) {
    is Outcome.Error -> UiText.Resource(R.string.error_with_code, listOf(code)) to
        (errorBody?.let(UiText::Raw) ?: UiText.Resource(R.string.error_unknown))
    is Outcome.Exception -> UiText.Resource(R.string.error) to
        UiText.Resource(R.string.error_unknown)
}
