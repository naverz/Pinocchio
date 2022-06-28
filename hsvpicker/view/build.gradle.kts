/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "io.github.naverz.hsvpicker.view"
    compileSdk = io.github.naverz.pinocchio.Versions.ANDROID_COMPILE_SDK_NO

    defaultConfig {
        minSdk = 19
        targetSdk = io.github.naverz.pinocchio.Versions.ANDROID_TARGET_SDK_NO

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
}

dependencies {
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.APP_COMPAT)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.CORE_KTX)
    testImplementation(io.github.naverz.pinocchio.Dependencies.Test.JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.EXT_JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.ESPRESSO_CORE)
}