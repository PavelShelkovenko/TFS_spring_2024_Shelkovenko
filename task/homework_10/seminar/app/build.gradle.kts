plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.example.testapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    viewBinding {
        enable = true
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
        animationsDisabled = true
    }
}

dependencies {
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // ViewBinding Delegates
    implementation(libs.viewbindingpropertydelegate.full)

    // Elmslie
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.android)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)

    // Retrofit
    implementation(libs.retrofit)

    // Andriod Test Rules
    implementation(libs.androidx.rules)

    // JUnit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Kotest
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)

    // Hamcrest Matchers
    androidTestImplementation(libs.hamcrest)

    // Kaspresso
    androidTestImplementation(libs.kaspresso)

    // Espresso Intents
    androidTestImplementation(libs.androidx.espresso.intents)

    // Wiremock
    debugImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }
}
