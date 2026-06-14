package com.alavpa.colors.data.infrastructure

import android.os.Bundle
import com.alavpa.colors.domain.infrastructure.AnalyticsManager
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsManagerImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsManager {

    override fun trackLevelStart(levelId: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.LEVEL, levelId)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, bundle)
    }

    override fun trackLevelComplete(levelId: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.LEVEL, levelId)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, bundle)
    }

    override fun setCurrentLevel(levelId: Int) {
        firebaseAnalytics.setUserProperty("current_level", levelId.toString())
    }

    override fun trackAppOpen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
    }

    override fun trackHintClicked() {
        firebaseAnalytics.logEvent("hint_clicked", null)
    }

    override fun trackHintConfirmed() {
        firebaseAnalytics.logEvent("hint_confirmed", null)
    }

    override fun trackLevelRetry(levelId: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.LEVEL, levelId)
        }
        firebaseAnalytics.logEvent("level_retry", bundle)
    }

    override fun trackMistakeOccurred(levelId: Int) {
        val bundle = Bundle().apply {
            putInt(FirebaseAnalytics.Param.LEVEL, levelId)
        }
        firebaseAnalytics.logEvent("mistake_occurred", bundle)
    }

    override fun trackAdRewardRequested(adUnit: String) {
        val bundle = Bundle().apply {
            putString("ad_unit", adUnit)
        }
        firebaseAnalytics.logEvent("ad_reward_requested", bundle)
    }

    override fun trackAdRewardReceived(adUnit: String) {
        val bundle = Bundle().apply {
            putString("ad_unit", adUnit)
        }
        firebaseAnalytics.logEvent("ad_reward_received", bundle)
    }
}
