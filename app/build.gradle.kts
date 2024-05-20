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
        buildConfigField("String", "API_URL", "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        flavorDimensions += "env"
        productFlavors {
            create("staging") {
                dimension = "env"
                buildConfigField("String", "API_URL", "\"http://localhost:8080\"")
            }
            create("development") {
                dimension = "env"
                buildConfigField("String", "API_URL", "\"https://tinkoff-android-spring-2024.zulipchat.com/api/v1/\"")
            }
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
            it.useJUnit()
        }
        animationsDisabled = true
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
    implementation(libs.androidx.navigation.testing)
    kapt(libs.dagger.compiler)

    // Room
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)

    // Glide
    implementation(libs.glide)
    kapt(libs.glide.compiler)

    // Android Test Rules
    implementation(libs.androidx.rules)

    // FragmentTest
    debugImplementation(libs.androidx.fragment.testing)

    // CoroutinesTest
    testImplementation(libs.kotlinx.coroutines.test)

    // JUnit
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.junit)

    // Kaspresso
    androidTestImplementation(libs.kaspresso)

    // Espresso Intents
    androidTestImplementation(libs.androidx.espresso.intents)

    // Hamcrest Matchers
    androidTestImplementation(libs.hamcrest)

    // Wiremock
    debugImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

}