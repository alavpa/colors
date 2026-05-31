package com.alavpa.colors.domain.infrastructure

interface AdManager {
    fun loadInterstitial(activity: Any)
    fun showInterstitial(activity: Any, onAdDismissed: () -> Unit)
    fun loadRewarded(activity: Any)
    fun showRewarded(activity: Any, onUserEarnedReward: () -> Unit, onAdDismissed: () -> Unit)
}
