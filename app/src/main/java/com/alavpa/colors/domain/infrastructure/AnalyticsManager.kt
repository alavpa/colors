package com.alavpa.colors.domain.infrastructure

interface AnalyticsManager {
    fun trackLevelStart(levelId: Int)
    fun trackLevelComplete(levelId: Int)
    fun setCurrentLevel(levelId: Int)
}
