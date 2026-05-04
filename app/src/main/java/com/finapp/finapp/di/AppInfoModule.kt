package com.finapp.finapp.di

import com.finapp.feature.common.info.AppInfoProvider
import com.finapp.finapp.info.AppInfoProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface AppInfoModule {
    @Binds
    fun bindAppInfoProvider(impl: AppInfoProviderImpl): AppInfoProvider
}
