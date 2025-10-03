// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val agp = "8.9.1"
    val kotlin = "2.2.20"
    id("com.android.application") version "$agp" apply false
    id("org.jetbrains.kotlin.android") version "$kotlin" apply false
    id("com.android.library") version "$agp" apply false
    id ("org.jetbrains.kotlin.plugin.compose") version "$kotlin" apply false
}