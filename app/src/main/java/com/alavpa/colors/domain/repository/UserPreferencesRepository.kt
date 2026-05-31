package com.alavpa.colors.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isAdsRemoved: Flow<Boolean>
    val remainingHints: Flow<Int>
    val currentLevel: Flow<Int>
    val isMuted: Flow<Boolean>

    suspend fun setAdsRemoved(removed: Boolean)
    suspend fun setRemainingHints(hints: Int)
    suspend fun setCurrentLevel(level: Int)
    suspend fun setMuted(muted: Boolean)
}
