[![](https://jitpack.io/v/devansh-argon/commons.svg)](https://jitpack.io/#devansh-argon/commons)

### Download

Gradle:
```gradle
dependencies {
  implementation("com.github.devansh-argon:commons:<latest-version>")
}
```

Maven:
```xml
	<dependency>
	    <groupId>com.github.devansh-argon</groupId>
	    <artifactId>commons</artifactId>
	    <version>v0.1.1</version>
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
    <color name="ad_bg_color">#ECFFFF</color>
    <color name="player_ad_bg_color">#ECFFFF</color>
    <color name="player_ad_button_color">#047FD6</color>
```
