
object Deps {

    private val versions = Versions

    val plugin = Plugin
    val kotlin = Kotlin
    val android = Android
    val compose = Compose
    val navigation = Navigation
    val okhttp = Okhttp
    val retrofit = Retrofit

    object Plugin {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
        const val android = "com.android.tools.build:gradle:${versions.androidPlugin}"
        const val androidApplication = "com.android.application"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinParcelize = "kotlin-parcelize"
        const val kotlinKapt = "kotlin-kapt"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlin}"
    }

    object Android {
        const val coreKtx = "androidx.core:core-ktx:${versions.core}"
        const val activityCompose = "androidx.activity:activity-compose:${versions.activityCompose}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${versions.lifecycle}"
        const val viewmodelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${versions.lifecycle}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${versions.constraint}"
        const val material = "com.google.android.material:material:${versions.material}"
        const val dataStore = "androidx.datastore:datastore-preferences:${versions.dataStore}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${versions.compose}"
        const val material = "androidx.compose.material3:material3:${versions.composeMaterial}"
        const val preview = "androidx.compose.ui:ui-tooling-preview:${versions.compose}"
        const val tooling = "androidx.compose.ui:ui-tooling:${versions.compose}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${versions.composeConstraintLayout}"
    }

    object Navigation {
        const val navigationCompose = "androidx.navigation:navigation-compose:${versions.navigationCompose}"
    }

    object Okhttp {
        const val client = "com.squareup.okhttp3:okhttp:${versions.okhttp}"
        const val logging = "com.squareup.okhttp3:logging-interceptor:${versions.okhttp}"
    }

    object Retrofit {
        const val client = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${versions.retrofit}"
    }

}