# App Startup
-keep class androidx.startup.** { *; }
-keep class * implements androidx.startup.Initializer {
    public <init>();
}

# Kotlin Serialization
-keepattributes *Annotation*, EnclosingMethod, InnerClasses
-keepclassmembernames class com.alavpa.colors.ui.navigation.** {
    *** Companion;
    *** $serializer;
}
-keep @kotlinx.serialization.Serializable class com.alavpa.colors.ui.navigation.** { *; }

# Hilt
# Hilt and ViewModels usually have bundled rules, but we keep these for safety if needed.
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
