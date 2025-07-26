package com.finapp.finapp.di

import android.content.Context
import com.finapp.core.data.impl.di.CoreDataModule
import com.finapp.core.data.impl.di.DataStoreModule
import com.finapp.core.database.impl.di.CoreDatabaseModule
import com.finapp.core.remote.impl.di.CoreRemoteModule
import com.finapp.core.settings.impl.di.CoreSettingsModule
import com.finapp.feature.account.di.FeatureAccountComponent
import com.finapp.feature.expenses.di.FeatureExpensesComponent
import com.finapp.feature.home.di.FeatureHomeComponent
import com.finapp.feature.income.di.FeatureIncomeComponent
import com.finapp.feature.tags.di.FeatureTagsComponent
import com.finapp.finapp.FinappApplication
import com.finapp.core.work.transaction.di.FinappWorkComponent
import com.finapp.core.work.transaction.di.WorkSupportModule
import com.finapp.feature.settings.di.FeatureSettingsComponent
import com.finapp.finapp.theme.di.FeatureMainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppInfoModule::class,
        CoreDataModule::class,
        CoreRemoteModule::class,
        CoreDatabaseModule::class,
        CoreSettingsModule::class,
        WorkSupportModule::class,
        DataStoreModule::class,
        FeatureHomeComponent.InstallationModule::class,
        FeatureAccountComponent.InstallationModule::class,
        FeatureIncomeComponent.InstallationModule::class,
        FeatureExpensesComponent.InstallationModule::class,
        FeatureTagsComponent.InstallationModule::class,
        FeatureSettingsComponent.InstallationModule::class,
        FeatureMainComponent.InstallationModule::class,
        FinappWorkComponent.InstallationModule::class,
    ]
)
interface AppComponent {
    fun inject(app: FinappApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        @BindsInstance
        fun application(app: FinappApplication): Builder

        fun build(): AppComponent
    }

    fun featureHomeComponentBuilder(): FeatureHomeComponent.Builder

    fun featureAccountComponentBuilder(): FeatureAccountComponent.Builder

    fun featureIncomeComponentBuilder(): FeatureIncomeComponent.Builder

    fun featureExpensesComponentBuilder(): FeatureExpensesComponent.Builder

    fun featureTagsComponentBuilder(): FeatureTagsComponent.Builder

    fun featureSettingsComponentBuilder(): FeatureSettingsComponent.Builder

    fun featureMainComponentBuilder(): FeatureMainComponent.Builder

    fun workerComponentBuilder(): FinappWorkComponent.Builder
}