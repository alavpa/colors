import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.alavpa.colors"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.alavpa.colors"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val appId =
            localProperties.getProperty("admob.app_id") ?: System.getenv("ADMOBAPPID_DEBUG") ?: ""
        manifestPlaceholders["admobAppId"] = appId

        buildConfigField(
            "String",
            "ADMOB_BANNER_ID",
            "\"${localProperties.getProperty("admob.banner_id") ?: System.getenv("ADMOB_BANNER_ID_DEBUG") ?: ""}\""
        )
        buildConfigField(
            "String",
            "ADMOB_INTERSTITIAL_ID",
            "\"${localProperties.getProperty("admob.interstitial_id") ?: System.getenv("ADMOB_INTERSTITIAL_ID_DEBUG") ?: ""}\""
        )
        buildConfigField(
            "String",
            "ADMOB_REWARDED_ID",
            "\"${localProperties.getProperty("admob.rewarded_id") ?: System.getenv("ADMOB_REWARDED_ID_DEBUG") ?: ""}\""
        )
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("RELEASE_KEYSTORE_PATH") ?: "perletagames.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            // For release builds, prioritize environment variables (e.g., GitHub Secrets)
            System.getenv("ADMOBAPPID")?.let { manifestPlaceholders["admobAppId"] = it }
            System.getenv("ADMOB_BANNER_ID")
                ?.let { buildConfigField("String", "ADMOB_BANNER_ID", "\"$it\"") }
            System.getenv("ADMOB_INTERSTITIAL_ID")
                ?.let { buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"$it\"") }
            System.getenv("ADMOB_REWARDED_ID")
                ?.let { buildConfigField("String", "ADMOB_REWARDED_ID", "\"$it\"") }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.play.services.ads)
    implementation(libs.androidx.datastore.preferences)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}