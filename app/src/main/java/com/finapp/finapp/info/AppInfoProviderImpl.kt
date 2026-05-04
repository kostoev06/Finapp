package com.finapp.finapp.info

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.finapp.feature.common.info.AppInfoProvider
import com.finapp.finapp.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInfoProviderImpl @Inject constructor(
    private val context: Context
) : AppInfoProvider {
    override val versionName: String = BuildConfig.VERSION_NAME
    override val versionCode: Long = BuildConfig.VERSION_CODE.toLong()

    /**
     * `lastUpdateTime` за пределами проверенного диапазона может быть `0L` на эмуляторах —
     * в этом случае откатываемся на `firstInstallTime`. `lazy` — потому что чтение
     * `PackageManager` дороже, чем нам нужно при каждом обращении.
     */
    override val lastUpdateMillis: Long by lazy {
        val pm = context.packageManager
        val pkg = context.packageName
        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(pkg, 0)
        }
        pi.lastUpdateTime.takeIf { it > 0L } ?: pi.firstInstallTime
    }
}
