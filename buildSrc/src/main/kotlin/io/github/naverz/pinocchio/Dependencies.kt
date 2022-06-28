package io.github.naverz.pinocchio

object Dependencies {
    object Test {
        const val JUNIT = "junit:junit:4.13.2"
        const val EXT_JUNIT = "androidx.test.ext:junit:1.1.3"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.4.0"
    }

    object Material{
        const val STANDARD = "com.google.android.material:material:1.4.0"
    }
    object AndroidX {
        const val APP_COMPAT = "androidx.appcompat:appcompat:1.4.2"
        const val CORE_KTX = "androidx.core:core-ktx:1.8.0"
        const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.1.1"

        object Compose {
            const val UI_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}"
            const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose:1.4.0"
            const val COMPOSE_FOUNDATION =
                "androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}"
        }
    }
}