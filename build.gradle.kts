import java.util.Properties

// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.sonar)
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

sonar {
    properties {
        property("sonar.projectKey", "com.alavpa:colors")
        property("sonar.projectName", "Colors")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", localProperties.getProperty("sonar.token") ?: "")
        property("sonar.scm.disabled", "true")
        property("sonar.androidLint.reportPaths", "app/build/reports/lint-results-debug.xml")
        property("sonar.inclusions", "**/*.kt, **/*.kts")
    }
}

tasks.named("sonar") {
    dependsOn(":app:lintDebug")
}

