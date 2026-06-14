package com.alavpa.colors.di

import android.content.Context
import com.alavpa.colors.data.infrastructure.AdManagerImpl
import com.alavpa.colors.data.infrastructure.AnalyticsManagerImpl
import com.alavpa.colors.data.infrastructure.SoundManagerImpl
import com.alavpa.colors.domain.infrastructure.AdManager
import com.alavpa.colors.domain.infrastructure.AnalyticsManager
import com.alavpa.colors.domain.infrastructure.SoundManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Binds
    @Singleton
    abstract fun bindAnalyticsManager(
        analyticsManagerImpl: AnalyticsManagerImpl
    ): AnalyticsManager

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }
    }
}
