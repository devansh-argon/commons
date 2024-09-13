[![](https://www.jitpack.io/v/Nirav186/commons.svg)](https://www.jitpack.io/#Nirav186/Nirav-Commons)

### Download

Gradle:
```gradle
dependencies {
  implementation("com.github.Nirav186:Nirav-Commons:<latest-version>")
}
```

Maven:
```xml
<dependency>
  <groupId>com.github.Nirav186</groupId>
  <artifactId>Nirav-Commons</artifactId>
  <version>latest-version</version>
</dependency>
```

Use of GDPR Dialog : (put it in Home activity)
```kotlin
CommonGdprDialog.checkGDPR(this) {
  App().getAdsFromRemoteConfig(this) {
    showBannerAd(binding.adView)
  }
}
```

Use this function to fetch remote configs :
```kotlin
    fun getAdsFromRemoteConfig(activity: Activity, onAdsInitialized: () -> Unit) {
        FirebaseApp.initializeApp(this)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(10000).build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        val jsonConfigKey = if (BuildConfig.DEBUG) "test_ids" else "real_ids"

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val json = remoteConfig.getString(jsonConfigKey)
                    CommonAdManager.init(
                        activity = activity,
                        application = this,
                        jsonString = json,
                        onAdsInitialized = onAdsInitialized
                    )
                } else {
                    Log.e("TAG", "Error occurred")
                }
            }
    }
```

Put below line in colors file to handle colors of button in exit dialog :
```kotlin
<color name="dialogThemeColor">put your theme color</color>
```
