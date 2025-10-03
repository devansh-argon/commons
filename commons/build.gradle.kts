plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

android {
    namespace = "com.nirav.commons"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        version = "0.5.0"
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
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
    //sdp

    //ads
    implementation("com.google.android.gms:play-services-ads:24.6.0")
    //ads

    //gson
    implementation("com.google.code.gson:gson:2.13.2")
    //gson

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-process:2.9.4")
    //lifecycle

    //compose
    implementation("androidx.activity:activity-compose:1.9.2")
    //compose

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
                version = "0.0.5"
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