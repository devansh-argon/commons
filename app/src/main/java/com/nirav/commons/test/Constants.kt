package com.nirav.commons.test

import com.google.gson.JsonObject
import com.nirav.commons.ads.AdModel

object Constants {

    val adModel = """
        {
        "appId": "ca-app-pub-3940256099942544~3347511713",
        "interstitialId": "ca-app-pub-3940256099942544/1033173712",
        "bannerId": "ca-app-pub-3940256099942544/6300978111",
        "nativeId": "ca-app-pub-3940256099942544/2247696110",
        "appOpenId": "ca-app-pub-3940256099942544/9257395921",
        "rewardId": "ca-app-pub-3940256099942544/5354046379",
        "isAppIdActive": true,
        "isInterstitialAdActive": true,
        "isBannerAdActive": true,
        "isNativeAdActive": true,
        "isAppOpenAdActive": false,
        "isRewardAdActive": false,
        "adsTimeInterval": 40000
    }
    """.trimIndent()

}