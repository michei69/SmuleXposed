// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
 //       maven(url = "https://api.xposed.info/")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://api.xposed.info/")
    }
}