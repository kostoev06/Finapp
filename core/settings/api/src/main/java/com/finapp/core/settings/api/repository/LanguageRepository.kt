package com.finapp.core.settings.api.repository

import com.finapp.core.settings.api.model.LanguageOption
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    val language: Flow<LanguageOption>
    suspend fun set(option: LanguageOption)
}
