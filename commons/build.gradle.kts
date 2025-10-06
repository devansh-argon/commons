plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

android {
    namespace = "com.nirav.commons"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        version = "0.5.1"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("com.google.android.gms:play-services-ads-api:24.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("androidx.compose.material:material:1.9.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.9.2")

    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    implementation("com.google.android.ump:user-messaging-platform:3.2.0")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    //gson
    implementation("com.google.code.gson:gson:2.13.2")
    //lifecycle
    implementation("androidx.lifecycle:lifecycle-process:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    //simmer layout
    implementation("com.facebook.shimmer:shimmer:0.5.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "com.github.devansh-argon"
                artifactId = "commons"
                version = "0.1.1"
            }
        }
//        repositories {
//            maven {
//                url = uri("https://maven.pkg.github.com/Nirav186/commons")
//                credentials {
//                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
//                    password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
//                }
//            }
//        }
    }
}