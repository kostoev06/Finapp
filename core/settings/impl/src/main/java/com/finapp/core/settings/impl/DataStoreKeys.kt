package com.finapp.core.settings.impl

import androidx.datastore.preferences.core.stringPreferencesKey

internal object DataStoreKeys {
    val THEME_MODE  = stringPreferencesKey("theme_mode")
    val BRAND_COLOR = stringPreferencesKey("brand_color")
}