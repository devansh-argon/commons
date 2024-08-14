package com.nirav.commons.ads

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.gson.Gson
import com.nirav.commons.CommonGdprDialog
import com.nirav.commons.R
import com.nirav.commons.databinding.DialogExitBinding

val TAG = "CommonAdManager"

object CommonAdManager {

    private var adModel = AdModel()

    private var nativeAd: NativeAd? = null
    var interstitialAd: InterstitialAd? = null
    var rewardedInterstitialAd: RewardedInterstitialAd? = null

    var lastTimeStampForInter: Long = 0
    fun isAdReadyToShow() =
        (System.currentTimeMillis() - lastTimeStampForInter) > adModel.adsTimeInterval

    fun init(
        activity: Activity,
        jsonString: String,
        onAdsInitialized: () -> Unit,
        application: Application
    ) {
        val gson = Gson()
        val adModel = gson.fromJson(jsonString, AdModel::class.java)
        Log.e("TAG111", "init: $adModel")
        this.adModel = adModel
        if (adModel.isAppIdActive) {
            MobileAds.initialize(activity)
            setAppId(
                activity = activity,
                onAdsInitialized = {
                    loadIntertitialAd(activity)
                    loadNativeAd(activity)
                    loadRewardedAd(activity)
                    onAdsInitialized()
                    if (CommonAdManager.adModel.isAppOpenAdActive) {
                        AppOpenAdManager(application, CommonAdManager.adModel.appOpenId, activity)
                    }
                }
            )
        }
    }

    fun initWithGdpr(
        activity: Activity,
        jsonString: String,
        onAdsInitialized: () -> Unit,
        application: Application
    ) {
        val gson = Gson()
        val adModel = gson.fromJson(jsonString, AdModel::class.java)
        Log.e("TAG111", "init: $adModel")
        if (adModel.isAppIdActive) {
            MobileAds.initialize(activity)
            setAppId(
                activity = activity,
                onAdsInitialized = {
                    CommonGdprDialog.checkGDPR(activity) {
                        this.adModel = adModel
                        loadIntertitialAd(activity)
                        loadNativeAd(activity)
                        loadRewardedAd(activity)
                        onAdsInitialized()
                        if (CommonAdManager.adModel.isAppOpenAdActive) {
                            AppOpenAdManager(
                                application,
                                CommonAdManager.adModel.appOpenId,
                                activity
                            )
                        }
                    }
                }
            )

        }
    }

    private fun setAppId(
        activity: Activity,
        onAdsInitialized: () -> Unit
    ) {
        try {
            val ai = activity.packageManager.getApplicationInfo(
                activity.packageName,
                PackageManager.GET_META_DATA
            )
            val bundle = ai.metaData
            val myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
            Log.d("TAG000", "Name Found: $myApiKey")
            ai.metaData.putString(
                "com.google.android.gms.ads.APPLICATION_ID",
                adModel.appId
            )
            Handler(Looper.getMainLooper()).postDelayed({
                onAdsInitialized()
            }, 1000)
        } catch (e: Exception) {
            Log.e("TAG000", "Failed to load meta-data, NameNotFound: " + e.message)
        }
    }

    fun loadIntertitialAd(
        context: Context,
        onAdLoaded: (() -> Unit)? = null,
        onAdLoadFailed: ((String) -> Unit)? = null,
    ) {
        if (adModel.isInterstitialAdActive.not()) return
        if (interstitialAd != null) return
        InterstitialAd.load(
            context,
            adModel.interstitialId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    interstitialAd = null
                    onAdLoadFailed?.invoke(adError.message)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.(interstitial)")
                    interstitialAd = ad
                    onAdLoaded?.invoke()
                }
            }
        )
    }

    fun Activity.showInterstitialAd() {
        if (!isAdReadyToShow()) return
        if (interstitialAd == null) {
            loadIntertitialAd(this)
        } else {
            interstitialAd?.show(this)
            lastTimeStampForInter = System.currentTimeMillis()
            interstitialAd = null
            loadIntertitialAd(this)
        }
    }

    fun Context.showBannerAd(frameLayout: FrameLayout) {
        if (adModel.isBannerAdActive.not()) return
        val adView = AdView(this)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adModel.bannerId
        frameLayout.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.e("TAG11111", "onAdFailedToLoad: " + p0.message)
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.e("TAG11111", "onAdLoaded: ")
            }
        }
    }

    private fun isNetworkAvailable(c: Context): Boolean {
        val manager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        return isAvailable
    }

    fun FrameLayout.showNativeAd() {
        nativeAd?.let {
            setBigNativeAd(this.context, this, true, it)
        } ?: run {
            loadAndShowNativeAd(this@showNativeAd.context)
        }
    }

    fun FrameLayout.showExitNativeAd() {
        nativeAd?.let {
            setBigNativeAd(this.context, this, true, it, true)
        }
    }

    private fun setBigNativeAd(
        context: Context,
        frameLayout: FrameLayout?,
        isShowMedia: Boolean = true,
        nativeAd: NativeAd,
        isExitDialog: Boolean = false
    ) {
        val inflater = LayoutInflater.from(context)
        val adView: NativeAdView = if (isExitDialog) {
            inflater.inflate(R.layout.exit_dialog_native_ad, null) as NativeAdView
        } else {
            inflater.inflate(R.layout.layout_big_native_ad_mob, null) as NativeAdView
        }
        if (frameLayout != null) {
            frameLayout.removeAllViews()
            frameLayout.addView(adView)
            frameLayout.visibility = View.VISIBLE
        }
        try {
            if (isShowMedia) {
                val mediaView = adView.findViewById<MediaView>(R.id.mediaView)
                nativeAd.mediaContent?.let { mediaView.mediaContent = it }
                mediaView.setOnHierarchyChangeListener(object :
                    ViewGroup.OnHierarchyChangeListener {
                    override fun onChildViewAdded(parent: View, child: View) {
                        if (child is ImageView) {
                            child.adjustViewBounds = true
                            child.scaleType = ImageView.ScaleType.FIT_CENTER
                        }
                    }

                    override fun onChildViewRemoved(parent: View, child: View) {}
                })
                adView.mediaView = mediaView
            }
            adView.headlineView = adView.findViewById(R.id.adTitle)
            adView.bodyView = adView.findViewById(R.id.adDescription)
            adView.iconView = adView.findViewById(R.id.adIcon)
            adView.advertiserView = adView.findViewById(R.id.adAdvertiser)
            adView.callToActionView = adView.findViewById(R.id.callToAction)
            (adView.headlineView as TextView).text = nativeAd.headline
            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = "\t\t\t" + nativeAd.body
            }
            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as Button).text = nativeAd.callToAction
            }
            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
                adView.iconView?.visibility = View.VISIBLE
            }
            if (isShowMedia) {
                adView.mediaView?.visibility = View.VISIBLE
            } else {
                adView.mediaView?.visibility = View.GONE
            }
            if (nativeAd.advertiser == null) {
                adView.advertiserView?.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView?.visibility = View.VISIBLE
            }

            val vc = nativeAd.mediaContent?.videoController
            vc?.mute(true)
            if (vc?.hasVideoContent() == true) {
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {}
            }
            nativeAd.let { adView.setNativeAd(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "populateUnifiedNativeAdView Exception: " + e.message)
        }
    }

    fun loadNativeAd(
        context: Context,
        onAdLoaded: (() -> Unit)? = null,
        onAdLoadFailed: ((String) -> Unit)? = null
    ) {
        if (adModel.isNativeAdActive) {
            if (nativeAd == null) {
                val builder: AdLoader.Builder?
                builder = AdLoader.Builder(context, adModel.nativeId)
                builder.forNativeAd(NativeAd.OnNativeAdLoadedListener { unifiedNativeAd: NativeAd ->
                    nativeAd = unifiedNativeAd
                })
                builder.withAdListener(object : AdListener() {
                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.d("TAG111", "onAdClosed:")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        Log.d("TAG111", "onAdFailedToLoad: ${loadAdError.message}")
                        onAdLoadFailed?.invoke(loadAdError.message)
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.d("TAG111", "onAdLoaded: ")
                        onAdLoaded?.invoke()
                    }
                })
                val videoOptions = VideoOptions.Builder()
                    .setStartMuted(true)
                    .build()
                val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
                builder.withNativeAdOptions(adOptions)
                val adLoader = builder.build()
                adLoader.loadAd(AdRequest.Builder().build())
                nativeAd = null
            }
        }
    }

    fun loadNewNativeAd(context: Context) {
        nativeAd = null
        loadNativeAd(context)
    }

    fun Activity.showExitDialog(withAd: Boolean = false) {
        val dialog = Dialog(this)
        val binding = DialogExitBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)
        dialog.window?.setBackgroundDrawable(inset)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnYes.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        if (withAd) {
            binding.frameLayoutAd.showExitNativeAd()
        }
        dialog.show()
    }

    fun showRewardAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdDismiss: (() -> Unit)? = null
    ) {
        if (rewardedInterstitialAd == null) {
            Toast.makeText(activity, "Ad is not loaded", Toast.LENGTH_SHORT).show()
            loadRewardedAd(activity)
        } else {
            rewardedInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        onAdDismiss?.invoke()
                    }
                }
            rewardedInterstitialAd?.show(
                activity
            ) {
                onRewardEarned()
            }
            loadRewardedAd(activity)
        }
    }

    private fun loadRewardedAd(context: Context) {
        if (adModel.isRewardAdActive.not()) return
        RewardedInterstitialAd.load(context, adModel.rewardId,
            AdRequest.Builder().build(), object :
                RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Ad was loaded.(reward)")
                    rewardedInterstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    rewardedInterstitialAd = null
                }
            })
    }

    fun FrameLayout.loadAndShowNativeAd(context: Context) {
        if (adModel.isNativeAdActive.not()) return
        val builder: AdLoader.Builder?
        builder = AdLoader.Builder(context, adModel.nativeId)
        builder.forNativeAd(NativeAd.OnNativeAdLoadedListener { unifiedNativeAd: NativeAd ->
            setBigNativeAd(
                context = context,
                frameLayout = this,
                nativeAd = unifiedNativeAd,
                isExitDialog = false
            )
        })
        builder.withAdListener(object : AdListener() {
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
        })
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()
        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()
        builder.withNativeAdOptions(adOptions)
        val adLoader = builder.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun isBannerAdIsEnabled() = adModel.isBannerAdActive

    fun getBannerId() = adModel.bannerId

    fun getNativeAd() = nativeAd

    fun isInterstitialAdInitialized() = interstitialAd != null
}