package com.finapp.core.database.impl.di

import android.content.Context
import androidx.room.Room
import com.finapp.core.database.api.source.AccountLocalSource
import com.finapp.core.database.api.source.CategoryLocalSource
import com.finapp.core.database.api.source.TransactionLocalSource
import com.finapp.core.database.impl.dao.AccountDao
import com.finapp.core.database.impl.dao.CategoryDao
import com.finapp.core.database.impl.dao.TransactionDao
import com.finapp.core.database.impl.source.RoomAccountLocalSource
import com.finapp.core.database.impl.source.RoomCategoryLocalSource
import com.finapp.core.database.impl.source.RoomTransactionLocalSource
import com.finapp.core.database.impl.FinappDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class CoreDatabaseModule {
    @Binds
    abstract fun bindTransactionLocalSource(
        impl: RoomTransactionLocalSource
    ): TransactionLocalSource

    @Binds
    abstract fun bindCategoryLocalSource(
        impl: RoomCategoryLocalSource
    ): CategoryLocalSource

    @Binds
    abstract fun bindAccountLocalSource(
        impl: RoomAccountLocalSource
    ): AccountLocalSource

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(context: Context): FinappDatabase =
            Room.databaseBuilder(
                context,
                FinappDatabase::class.java,
                "finapp.db"
            )
                .fallbackToDestructiveMigration(false)
                .build()

        @Provides
        fun provideTransactionDao(db: FinappDatabase): TransactionDao =
            db.transactionDao()

        @Provides
        fun provideCategoryDao(db: FinappDatabase): CategoryDao =
            db.categoryDao()

        @Provides
        fun provideAccountDao(db: FinappDatabase): AccountDao =
            db.accountDao()
    }
}