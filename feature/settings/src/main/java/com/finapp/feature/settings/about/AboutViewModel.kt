package com.finapp.feature.settings.about

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import com.finapp.feature.common.info.AppInfoProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

data class AboutUiState(
    val versionName: String,
    val versionCode: Long,
    val lastUpdateMillis: Long
)

class AboutViewModel @AssistedInject constructor(
    appInfo: AppInfoProvider,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: AboutUiState = AboutUiState(
        versionName = appInfo.versionName,
        versionCode = appInfo.versionCode,
        lastUpdateMillis = appInfo.lastUpdateMillis
    )

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AboutViewModel>
}
