package com.finapp.feature.settings.about

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.finapp.feature.common.di.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Named

data class AboutUiState(
    val versionName: String,
    val versionCode: Long,
    val lastUpdateMillis: Long
)

class AboutViewModel @AssistedInject constructor(
    @Named("appVersionName") private val versionName: String,
    @Named("appVersionCode") private val versionCode: Long,
    @Named("appLastUpdateMillis") private val lastUpdateMillis: Long,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState: AboutUiState = AboutUiState(
        versionName = versionName,
        versionCode = versionCode,
        lastUpdateMillis = lastUpdateMillis
    )

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<AboutViewModel>
}