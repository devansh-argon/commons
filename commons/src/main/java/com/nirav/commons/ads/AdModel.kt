package com.nirav.commons.ads

import com.google.gson.annotations.SerializedName

data class AdModel(
    @SerializedName("appId") val appId: String = "",
    @SerializedName("interstitialId") val interstitialId: String = "",
    @SerializedName("bannerId") val bannerId: String = "",
    @SerializedName("nativeId") val nativeId: String = "",
    @SerializedName("appOpenId") val appOpenId: String = "",
    @SerializedName("rewardId") val rewardId: String = "",
    @SerializedName("rewardInterstitialId") val rewardInterstitialId: String = "",
    @SerializedName("isAppIdActive") val isAppIdActive: Boolean = false,
    @SerializedName("isInterstitialAdActive") val isInterstitialAdActive: Boolean = false,
    @SerializedName("isBannerAdActive") val isBannerAdActive: Boolean = false,
    @SerializedName("isNativeAdActive") val isNativeAdActive: Boolean = false,
    @SerializedName("isAppOpenAdActive") val isAppOpenAdActive: Boolean = false,
    @SerializedName("isRewardAdActive") val isRewardAdActive: Boolean = false,
    @SerializedName("isRewardInterstitialAdActive") val isRewardInterstitialAdActive: Boolean = false,
    @SerializedName("adsTimeInterval") val adsTimeInterval: Long = 0
)