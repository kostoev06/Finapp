package com.finapp.feature.tags.di

import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.di.ViewModelKey
import com.finapp.feature.tags.TagsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TagsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(TagsViewModel::class)
    fun bindTags(factory: TagsViewModel.Factory): ViewModelAssistedFactory<out ViewModel>
}