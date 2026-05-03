package com.finapp.finapp.di

import com.finapp.feature.common.haptics.HapticsPlayer
import com.finapp.feature.common.haptics.HapticsPlayerImpl
import com.finapp.feature.common.sound.SoundPlayer
import com.finapp.feature.common.sound.SoundPlayerImpl
import dagger.Binds
import dagger.Module

/**
 * Биндинги UI-эффектов (звук + хаптики) — обе реализации Singleton'ы из feature:common.
 */
@Module
interface SoundModule {
    @Binds
    fun bindSoundPlayer(impl: SoundPlayerImpl): SoundPlayer

    @Binds
    fun bindHapticsPlayer(impl: HapticsPlayerImpl): HapticsPlayer
}
