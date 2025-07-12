package com.finapp.core.data.impl.di

import com.finapp.core.data.api.repository.AccountRepository
import com.finapp.core.data.api.repository.CategoryRepository
import com.finapp.core.data.api.repository.CurrencyRepository
import com.finapp.core.data.api.repository.TransactionRepository
import com.finapp.core.data.impl.repository.AccountRepositoryImpl
import com.finapp.core.data.impl.repository.CategoryRepositoryImpl
import com.finapp.core.data.impl.repository.CurrencyRepositoryImpl
import com.finapp.core.data.impl.repository.TransactionRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface CoreDataModule {
    @Binds
    fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

}