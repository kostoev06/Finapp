package com.finapp.finapp.di

import com.finapp.feature.common.sound.SoundPlayer
import com.finapp.feature.common.sound.SoundPlayerImpl
import dagger.Binds
import dagger.Module

@Module
interface SoundModule {
    @Binds
    fun bindSoundPlayer(impl: SoundPlayerImpl): SoundPlayer
}
