package com.alavpa.colors.ui.ads

import android.app.Activity
import com.alavpa.colors.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdManager @Inject constructor() {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    fun loadInterstitial(activity: Activity) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
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

    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                    interstitialAd = null
                    onAdDismissed()
                }
            }
            interstitialAd?.show(activity)
        } else {
            onAdDismissed()
        }
    }

    fun loadRewarded(activity: Activity) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            activity,
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

    fun showRewarded(activity: Activity, onUserEarnedReward: () -> Unit, onAdDismissed: () -> Unit) {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                    rewardedAd = null
                    onAdDismissed()
                }
            }
            rewardedAd?.show(activity) {
                onUserEarnedReward()
            }
        } else {
            onAdDismissed()
        }
    }
}
