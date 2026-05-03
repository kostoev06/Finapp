package com.finapp.core.settings.impl

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object DataStoreKeys {
    val THEME_MODE  = stringPreferencesKey("theme_mode")
    val BRAND_COLOR = stringPreferencesKey("brand_color")
    val PASSCODE_HASH = stringPreferencesKey("passcode_hash")
    val PASSCODE_SALT = stringPreferencesKey("passcode_salt")
    val LANGUAGE = stringPreferencesKey("language")
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
}
