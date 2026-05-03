package com.finapp.core.settings.api.model

/**
 * Поддерживаемые языки приложения.
 *
 * @property tag BCP-47 tag, передаётся в `LocaleListCompat.forLanguageTags`.
 */
enum class LanguageOption(val tag: String) {
    RU("ru"),
    EN("en");

    companion object {
        /** Дефолт по системной локали: русский → [RU], всё остальное → [EN]. */
        fun fromSystem(systemLanguageTag: String): LanguageOption =
            if (systemLanguageTag.startsWith("ru", ignoreCase = true)) RU else EN
    }
}
