package com.alavpa.colors.data.infrastructure

import android.app.Activity
import com.alavpa.colors.BuildConfig
import com.alavpa.colors.domain.infrastructure.AdManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManagerImpl @Inject constructor() : AdManager {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    override fun loadInterstitial(activity: Any) {
        val currentActivity = activity as? Activity ?: return
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            currentActivity,
            BuildConfig.ADMOB_INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    override fun showInterstitial(activity: Any, onAdDismissed: () -> Unit) {
        val currentActivity = activity as? Activity ?: return
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial(currentActivity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                    interstitialAd = null
                    onAdDismissed()
                }
            }
            interstitialAd?.show(currentActivity)
        } else {
            onAdDismissed()
        }
    }

    override fun loadRewarded(activity: Any) {
        val currentActivity = activity as? Activity ?: return
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            currentActivity,
            BuildConfig.ADMOB_REWARDED_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    override fun showRewarded(activity: Any, onUserEarnedReward: () -> Unit, onAdDismissed: () -> Unit) {
        val currentActivity = activity as? Activity ?: return
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded(currentActivity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                    rewardedAd = null
                    onAdDismissed()
                }
            }
            rewardedAd?.show(currentActivity) {
                onUserEarnedReward()
            }
        } else {
            onAdDismissed()
        }
    }
}
