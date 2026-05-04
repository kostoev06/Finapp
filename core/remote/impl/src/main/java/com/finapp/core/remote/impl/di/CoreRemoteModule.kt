package com.finapp.core.remote.impl.di

import com.finapp.core.remote.api.source.AccountRemoteSource
import com.finapp.core.remote.api.source.CategoryRemoteSource
import com.finapp.core.remote.api.source.TransactionRemoteSource
import com.finapp.core.remote.impl.source.RetrofitAccountRemoteSource
import com.finapp.core.remote.impl.source.RetrofitCategoryRemoteSource
import com.finapp.core.remote.impl.source.RetrofitTransactionRemoteSource
import dagger.Binds
import dagger.Module

@Module(includes = [RetrofitModule::class])
abstract class CoreRemoteModule {
    @Binds
    abstract fun bindCategoryRemoteSource(impl: RetrofitCategoryRemoteSource): CategoryRemoteSource

    @Binds
    abstract fun bindAccountRemoteSource(impl: RetrofitAccountRemoteSource): AccountRemoteSource

    @Binds
    abstract fun bindTransactionRemoteSource(impl: RetrofitTransactionRemoteSource): TransactionRemoteSource
}
