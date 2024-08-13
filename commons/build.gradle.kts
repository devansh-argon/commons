plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.nirav.commons"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.compose.material:material:1.6.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.2")

    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    implementation("com.google.android.ump:user-messaging-platform:2.2.0")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    //sdp

    //ads
    implementation("com.google.android.gms:play-services-ads:23.3.0")
    //ads

    //gson
    implementation("com.google.code.gson:gson:2.10.1")
    //gson

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    //lifecycle

    //compose
    implementation("androidx.activity:activity-compose:1.8.2")
    //compose
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "com.github.Nirav186"
                artifactId = "commons"
                version = "0.3.0"
            }
        }
    }
}