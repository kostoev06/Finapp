package com.finapp.finapp.di

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import com.finapp.finapp.BuildConfig

@Module
class AppInfoModule {
    @Provides
    @Singleton
    @Named("appVersionName")
    fun provideAppVersionName(): String = BuildConfig.VERSION_NAME

    @Provides
    @Singleton
    @Named("appVersionCode")
    fun provideAppVersionCode(): Long = BuildConfig.VERSION_CODE.toLong()

    @Provides
    @Singleton
    @Named("appLastUpdateMillis")
    fun provideLastUpdateMillis(context: Context): Long {
        val pm = context.packageManager
        val pkg = context.packageName
        val pi = if (Build.VERSION.SDK_INT >= 33) {
            pm.getPackageInfo(pkg, PackageManager.PackageInfoFlags.of(0))
        } else {
            pm.getPackageInfo(pkg, 0)
        }
        val last = pi.lastUpdateTime
        val first = pi.firstInstallTime
        return if (last > 0L) last else first
    }
}