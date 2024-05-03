plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.pavelshelkovenko.tfs_spring_2024_shelkovenko"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pavelshelkovenko.tfs_spring_2024_shelkovenko"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
}

dependencies {
    implementation(libs.bundles.core)
    implementation(libs.bundles.ui.common)
    implementation(libs.bundles.navigation)
    implementation(libs.bundles.network)
    implementation(libs.bundles.elmslie)

    // DI
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}