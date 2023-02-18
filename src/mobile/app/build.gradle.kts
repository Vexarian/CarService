plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Versions.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "mobile.solareye.carservice"
        minSdk = Versions.MIN_SDK_VERSION
        targetSdk = Versions.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("mock") {
            initWith(getByName("debug"))
        }
        create("debugRelease") {
            initWith(getByName("debug"))
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(Deps.android.coreKtx)
    implementation(Deps.android.lifecycle)
    implementation(Deps.android.viewmodelCompose)
    implementation(Deps.android.activityCompose)
    implementation(Deps.android.constraint)
    implementation(Deps.android.material) {
        exclude(group = "androidx.annotation")
        exclude(group = "androidx.constraintlayout")
        exclude(group = "androidx.core")
        exclude(group = "androidx.fragment")
        exclude(group = "androidx.lifecycle")
        exclude(group = "androidx.recyclerview")
        exclude(group = "androidx.transition")
    }
    implementation(Deps.android.dataStore)

    implementation(Deps.compose.ui)
    implementation(Deps.compose.material)
    implementation(Deps.compose.preview)
    implementation(Deps.compose.constraintLayout)
    debugImplementation(Deps.compose.tooling)

    implementation(Deps.navigation.navigationCompose)

    implementation(Deps.okhttp.client)
    implementation(Deps.okhttp.logging)

    implementation(Deps.retrofit.client)
    implementation(Deps.retrofit.gson)

}