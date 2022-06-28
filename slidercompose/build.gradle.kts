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
    namespace = "io.github.naverz.pinocchio.slider.compose"
    compileSdk = io.github.naverz.pinocchio.Versions.ANDROID_COMPILE_SDK_NO

    defaultConfig {
        minSdk = 21
        targetSdk = io.github.naverz.pinocchio.Versions.ANDROID_TARGET_SDK_NO

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
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
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = io.github.naverz.pinocchio.Versions.COMPOSE_VERSION
    }
}

dependencies {
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.ACTIVITY_COMPOSE)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.UI_TOOLING)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.COMPOSE_FOUNDATION)
    testImplementation(io.github.naverz.pinocchio.Dependencies.Test.JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.EXT_JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.ESPRESSO_CORE)
}