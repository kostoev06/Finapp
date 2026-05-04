package com.finapp.feature.common.info

/**
 * Сводная информация о приложении: версия и время последнего апдейта.
 *
 * Интерфейс существует, чтобы экраны (например, About) не зависели ни от `BuildConfig`,
 * ни от `PackageManager`. Реализация живёт в `:app`.
 */
interface AppInfoProvider {
    val versionName: String
    val versionCode: Long

    /**
     * Unix-миллисекунды времени установки/последнего апдейта приложения. На свежеустановленном
     * билде совпадает с временем первой инсталляции.
     */
    val lastUpdateMillis: Long
}
