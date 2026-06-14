import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
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

    defaultConfig {
        applicationId = "com.alavpa.colors"
        minSdk = 24
        targetSdk = 37
        compileSdk = 37
        versionCode = System.getenv("VERSION_CODE")?.toInt() ?: 1
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setAdMobKeys(
            appIdKey = "debug.admob.app_id",
            appIdEnv = "ADMOBAPPID",
            bannerIdKey = "debug.admob.banner_id",
            bannerIdEnv = "ADMOB_BANNER_ID",
            interstitialIdKey = "debug.admob.interstitial_id",
            interstitialIdEnv = "ADMOB_INTERSTITIAL_ID",
            rewardedIdKey = "debug.admob.rewarded_id",
            rewardedIdEnv = "ADMOB_REWARDED_ID"
        )
    }

    signingConfigs {
        create("release") {
            storeFile = file(
                getLocalPropertyOrSystemEnv(
                    "keystorePath",
                    "RELEASE_KEYSTORE_PATH",
                    "perletagames.jks"
                )
            )
            storePassword = getLocalPropertyOrSystemEnv(
                "storePassword",
                "RELEASE_KEYSTORE_PASSWORD"
            )
            keyAlias = getLocalPropertyOrSystemEnv("keyAlias", "RELEASE_KEY_ALIAS")
            keyPassword = getLocalPropertyOrSystemEnv("keyPassword", "RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["appName"] = "Colors (Debug)"
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "Colors"

            setAdMobKeys(
                appIdKey = "admob.app_id",
                appIdEnv = "ADMOBAPPID",
                bannerIdKey = "admob.banner_id",
                bannerIdEnv = "ADMOB_BANNER_ID",
                interstitialIdKey = "admob.interstitial_id",
                interstitialIdEnv = "ADMOB_INTERSTITIAL_ID",
                rewardedIdKey = "admob.rewarded_id",
                rewardedIdEnv = "ADMOB_REWARDED_ID",
                prioritizeEnv = true
            )
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

fun com.android.build.api.dsl.VariantDimension.setAdMobKeys(
    appIdKey: String?,
    appIdEnv: String,
    bannerIdKey: String?,
    bannerIdEnv: String,
    interstitialIdKey: String?,
    interstitialIdEnv: String,
    rewardedIdKey: String?,
    rewardedIdEnv: String,
    prioritizeEnv: Boolean = false
) {
    manifestPlaceholders["admobAppId"] =
        getLocalPropertyOrSystemEnv(appIdKey, appIdEnv, prioritizeEnv = prioritizeEnv)
    buildConfigField(
        "String",
        "ADMOB_BANNER_ID",
        "\"${
            getLocalPropertyOrSystemEnv(
                bannerIdKey,
                bannerIdEnv,
                prioritizeEnv = prioritizeEnv
            )
        }\""
    )
    buildConfigField(
        "String",
        "ADMOB_INTERSTITIAL_ID",
        "\"${
            getLocalPropertyOrSystemEnv(
                interstitialIdKey,
                interstitialIdEnv,
                prioritizeEnv = prioritizeEnv
            )
        }\""
    )
    buildConfigField(
        "String",
        "ADMOB_REWARDED_ID",
        "\"${
            getLocalPropertyOrSystemEnv(
                rewardedIdKey,
                rewardedIdEnv,
                prioritizeEnv = prioritizeEnv
            )
        }\""
    )
}

fun getLocalPropertyOrSystemEnv(
    localPropertyKey: String?,
    systemEnvironmentKey: String,
    defaultValue: String = "",
    prioritizeEnv: Boolean = false
): String {
    if (prioritizeEnv) {
        System.getenv(systemEnvironmentKey)?.let { return it }
    }
    return localProperties.getProperty(localPropertyKey)
        ?: System.getenv(systemEnvironmentKey)
        ?: defaultValue
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
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    ksp(libs.hilt.compiler)
    ksp(libs.metadata.library)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}