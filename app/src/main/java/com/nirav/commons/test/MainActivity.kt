package com.nirav.commons.test

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.nirav.commons.ads.CommonAdManager.loadAndShowNativeAdHorizontally
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val frameLayout = findViewById<FrameLayout>(R.id.adFrameLayout)

        MobileAds.initialize(this) {
            App().initAds(
                activity = this@MainActivity,
                onAdsInitialized = {
                    lifecycleScope.launch {
                        frameLayout.loadAndShowNativeAdHorizontally(
                            context = this@MainActivity,
                            isBig = true,
                        )
                    }
                }
            )
        }
    }
}