package com.finapp.feature.account.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.account.AccountViewModel
import com.finapp.feature.account.edit.AccountEditViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AccountViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccount(factory: AccountViewModel.Factory): ViewModelAssistedFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AccountEditViewModel::class)
    fun bindAccountEdit(factory: AccountEditViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}