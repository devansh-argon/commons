package com.nirav.commons.test

import android.app.Activity
import android.app.Application
import com.nirav.commons.ads.CommonAdManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    fun initAds(activity: Activity, onAdsInitialized: () -> Unit) {
        CommonAdManager.init(
            activity = activity,
            jsonString = Constants.adModel,
            onAdsInitialized = onAdsInitialized,
            application = this
        )
    }

}