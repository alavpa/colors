package com.alavpa.colors.di

import com.alavpa.colors.data.infrastructure.AdManagerImpl
import com.alavpa.colors.data.infrastructure.SoundManagerImpl
import com.alavpa.colors.domain.infrastructure.AdManager
import com.alavpa.colors.domain.infrastructure.SoundManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {

    @Binds
    @Singleton
    abstract fun bindAdManager(
        adManagerImpl: AdManagerImpl
    ): AdManager

    @Binds
    @Singleton
    abstract fun bindSoundManager(
        soundManagerImpl: SoundManagerImpl
    ): SoundManager
}
