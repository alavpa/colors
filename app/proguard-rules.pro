# App Startup
-keep class androidx.startup.** { *; }
-keep class * implements androidx.startup.Initializer {
    public <init>();
}

# Kotlin Serialization
-keepattributes *Annotation*, EnclosingMethod, InnerClasses
-keep,allowobfuscation,allowshrinking class kotlinx.serialization.json.** { *; }
-keepclassmembernames class com.alavpa.colors.ui.navigation.** {
    *** Companion;
    *** $serializer;
}
-keep @kotlinx.serialization.Serializable class com.alavpa.colors.ui.navigation.** { *; }

# Hilt
-keep class com.google.dagger.hilt.** { *; }
-keep @com.google.dagger.hilt.android.lifecycle.HiltViewModel class *
-keep class * extends androidx.lifecycle.ViewModel

# AdMob
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }

# Firebase
-keep class com.google.firebase.** { *; }
