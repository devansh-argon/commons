package com.nirav.commons

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object GdprDialog {

    fun checkGDPR(activity: Activity, initAds: () -> Unit) {
        // Create a ConsentRequestParameters object.
        val params = ConsentRequestParameters
            .Builder()
            .build()

        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    // Consent gathering failed.
                    Log.w(
                        "SplashScreen", String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                    if (consentInformation.canRequestAds()) {
                        initAds()
                    }
                }
            },
            { requestConsentError ->
                // Consent gathering failed.
                Log.w(
                    "SplashScreen", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
            })

        if (consentInformation.canRequestAds()) {
            initAds()
        }
    }

}