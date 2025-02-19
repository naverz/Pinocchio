/**
 * Pinocchio
 * Copyright (c) 2022-present NAVER Z Corp.
 * Apache-2.0
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}
android {
    namespace = "io.github.naverz.hsvpicker.sample"
    compileSdk = io.github.naverz.pinocchio.Versions.ANDROID_COMPILE_SDK_NO

    defaultConfig {
        applicationId = "io.github.naverz.hsvpicker.sample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
}

dependencies {
    implementation(project(":hsvpicker:compose"))
    implementation(project(":hsvpicker:view"))
    implementation(io.github.naverz.pinocchio.Dependencies.Material.STANDARD)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.CONSTRAINT_LAYOUT)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.APP_COMPAT)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.CORE_KTX)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.ACTIVITY_COMPOSE)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.UI_TOOLING)
    implementation(io.github.naverz.pinocchio.Dependencies.AndroidX.Compose.COMPOSE_FOUNDATION)
    testImplementation(io.github.naverz.pinocchio.Dependencies.Test.JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.EXT_JUNIT)
    androidTestImplementation(io.github.naverz.pinocchio.Dependencies.Test.ESPRESSO_CORE)
}