package com.alavpa.colors.di

import com.alavpa.colors.data.repository.ColorRepositoryImpl
import com.alavpa.colors.data.repository.LevelRepositoryImpl
import com.alavpa.colors.data.repository.UserPreferencesRepositoryImpl
import com.alavpa.colors.domain.repository.ColorRepository
import com.alavpa.colors.domain.repository.LevelRepository
import com.alavpa.colors.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindColorRepository(
        colorRepositoryImpl: ColorRepositoryImpl
    ): ColorRepository

    @Binds
    @Singleton
    abstract fun bindLevelRepository(
        levelRepositoryImpl: LevelRepositoryImpl
    ): LevelRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
