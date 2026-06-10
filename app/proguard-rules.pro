# App Startup
-keep class androidx.startup.** { *; }
-keep class * implements androidx.startup.Initializer {
    public <init>();
}

# Kotlin Serialization
-keepattributes *Annotation*, EnclosingMethod, InnerClasses, Signature
-keepclassmembernames class com.alavpa.colors.ui.navigation.** {
    *** Companion;
    *** $serializer;
}
-keep @kotlinx.serialization.Serializable class com.alavpa.colors.ui.navigation.** { *; }

# Hilt
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# WorkManager
-keep class androidx.work.impl.WorkDatabase_Impl { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker

# Room
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-dontwarn androidx.room.**

# General
-keepattributes SourceFile,LineNumberTable
