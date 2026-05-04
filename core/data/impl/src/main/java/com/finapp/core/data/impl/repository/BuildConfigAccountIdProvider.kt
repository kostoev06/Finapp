package com.finapp.core.data.impl.repository

import com.finapp.core.data.api.repository.AccountIdProvider
import com.finapp.core.data.impl.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildConfigAccountIdProvider @Inject constructor() : AccountIdProvider {
    override fun get(): Long = BuildConfig.ACCOUNT_ID
}
