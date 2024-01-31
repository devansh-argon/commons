package com.nirav.commons

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.consent.ConsentForm
import com.google.ads.consent.ConsentFormListener
import com.google.ads.consent.ConsentInfoUpdateListener
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import com.google.ads.consent.DebugGeography
import java.net.URL


class MainActivity : AppCompatActivity() {

    var form: ConsentForm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        form = ConsentForm.Builder(this, URL("https://www.google.com/"))
            .withListener(object : ConsentFormListener() {
                override fun onConsentFormLoaded() {
                    // Consent form loaded successfully.
                    Log.d("SplashScreen", "Consent form Loaded ")
                    form?.show()
                }

                override fun onConsentFormOpened() {
                    // Consent form was displayed.
                    Log.d("SplashScreen", "Consent form opened ")
                }

                override fun onConsentFormClosed(
                    consentStatus: ConsentStatus, userPrefersAdFree: Boolean
                ) {
                    // Consent form was closed.
                    Log.d("SplashScreen", "Consent form Closed ")
                }

                override fun onConsentFormError(errorDescription: String) {
                    // Consent form error.
                    Log.d("SplashScreen", "Consent form error $errorDescription")
                }
            })
            .withPersonalizedAdsOption()
            .withNonPersonalizedAdsOption()
            .build()
        val consentInformation: ConsentInformation = ConsentInformation.getInstance(this)

        val publisherIds = arrayOf(
            "ca-app-pub-3940256099942544~3347511713",
            "ca-app-pub-3940256099942544/9257395921",
            "ca-app-pub-3940256099942544/9214589741",
            "ca-app-pub-3940256099942544/6300978111",
            "ca-app-pub-3940256099942544/1033173712",
            "ca-app-pub-3940256099942544/5224354917",
            "ca-app-pub-3940256099942544/5224354917",
            "ca-app-pub-3940256099942544/5354046379",
            "ca-app-pub-3940256099942544/2247696110",
        )

        Log.e(
            "SplashScreen", "onCreate: " + Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        )

        consentInformation.addTestDevice("36b342a70a257c13")
        consentInformation.debugGeography = DebugGeography.DEBUG_GEOGRAPHY_EEA

        consentInformation.requestConsentInfoUpdate(
            publisherIds,
            object : ConsentInfoUpdateListener {
                override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {
                    form?.load()
                    Log.d("SplashScreen", "onConsentInfoUpdated: $consentStatus")
                }

                override fun onFailedToUpdateConsentInfo(reason: String?) {
                    Log.d("SplashScreen", "onFailedToUpdateConsentInfo: $reason")
                }
            }
        )
    }
}