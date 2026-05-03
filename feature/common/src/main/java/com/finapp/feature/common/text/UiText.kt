package com.finapp.feature.common.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Презентационная обёртка для строки, которую нужно отрисовать в UI.
 *
 * Позволяет ViewModel'ям возвращать "ссылку" на строку, не зная про `Context`/`Resources`,
 * чтобы итоговая локализация выполнялась уже в Composable. Благодаря этому строки в state
 * корректно перерисовываются при смене языка приложения — `Composable`-резолв опирается на
 * актуальную `LocalContext.current`.
 */
sealed interface UiText {
    /** Идентификатор ресурса с опциональными аргументами форматирования. */
    data class Resource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : UiText

    /** Сырая строка — для значений извне (например, тело ошибки с бэкенда). */
    data class Raw(val value: String) : UiText
}

/** Резолв вне Composable — нужен для Snackbar и прочих suspend-API. */
fun UiText.asString(context: Context): String = when (this) {
    is UiText.Resource ->
        if (args.isEmpty()) context.getString(resId)
        else context.getString(resId, *args.toTypedArray())
    is UiText.Raw -> value
}

/** Резолв внутри Composable — реактивно перерисуется при смене конфигурации. */
@Composable
fun UiText.asString(): String = asString(LocalContext.current)
