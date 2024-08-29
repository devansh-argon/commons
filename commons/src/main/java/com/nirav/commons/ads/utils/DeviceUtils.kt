package com.nirav.commons.ads.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import com.google.android.gms.ads.AdSize

fun Context.getAdaptiveBannerWidth(): AdSize {
    val display: Display = getSystemService(WindowManager::class.java).defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)

    val widthPixels = outMetrics.widthPixels.toFloat()
    val density = outMetrics.density

    val adWidth = (widthPixels / density).toInt()

    // Step 3 - Get adaptive ad size and return for setting on the ad view.
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}