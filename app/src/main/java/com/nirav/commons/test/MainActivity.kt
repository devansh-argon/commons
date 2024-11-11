package com.nirav.commons.test

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.nirav.commons.ads.CommonAdManager.loadAndShowNativeAd

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frameLayout = findViewById<FrameLayout>(R.id.adFrameLayout)

        App().initAds(
            activity = this,
            onAdsInitialized = {
                frameLayout.loadAndShowNativeAd(
                    context = this,
                    isBig = false
                )
//                showAdaptiveBannerAd(frameLayout,true)
            }
        )
    }
}