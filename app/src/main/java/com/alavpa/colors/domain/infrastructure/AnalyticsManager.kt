package com.alavpa.colors.domain.infrastructure

interface AnalyticsManager {
    fun trackLevelStart(levelId: Int)
    fun trackLevelComplete(levelId: Int)
    fun setCurrentLevel(levelId: Int)
    fun trackAppOpen()
    fun trackHintClicked()
    fun trackHintConfirmed()
    fun trackLevelRetry(levelId: Int)
    fun trackMistakeOccurred(levelId: Int)
    fun trackAdRewardRequested(adUnit: String)
    fun trackAdRewardReceived(adUnit: String)
    fun trackMuteToggled(isMuted: Boolean)
}
