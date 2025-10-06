package com.nirav.commons.ads.utils

import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

class DefaultAdListener : AdListener() {
    override fun onAdClosed() {
        super.onAdClosed()
        Log.d("TAG111", "onAdClosed:")
    }

    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
        super.onAdFailedToLoad(loadAdError)
        Log.d("TAG111", "onAdFailedToLoad: ${loadAdError.message}")
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        Log.d("TAG111", "onAdLoaded: ")
    }
}