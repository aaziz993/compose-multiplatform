# ===================================================
# 1. Kotlin Essentials
# ===================================================
# Keep Kotlin metadata for reflection/runtime
-keepclassmembers class kotlin.Metadata { *; }

# Keep reflection for suspend functions & coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Keep reflection for Kotlin serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }



# ===================================================
# 2. Jetpack Compose / UI
# ===================================================
# Keep @Composable functions
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep Compose runtime classes required for reflection
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }

# Optional: keep only runtime reflection entry points, not all UI
# This can break animations/inspection if too aggressive:
#-keep class androidx.compose.runtime.ComposableKt { *; }

# ===================================================
# 3. Android & App Essentials
# ===================================================
# Keep Android entry points (activities, services, receivers)
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep Application class
-keep class * extends android.app.Application { *; }

# Keep annotations
-keepattributes *Annotation*

# Keep manifest references
-keepclassmembers class * {
    @androidx.annotation.Keep <fields>;
    @androidx.annotation.Keep <methods>;
}

# ===================================================
# 4. Libraries
# ===================================================
# Google Play Services & Firebase
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

# Ktor & OkHttp
-keep class io.ktor.** { *; }
-keep class okhttp3.** { *; }

# ===================================================
# 5. WebView JS interfaces
# ===================================================
#-keepclassmembers class fqcn.of.javascript.interface.for.webview { public *; }

# ===================================================
# 6. Aggressive shrinking / obfuscation
# ===================================================
# Adapt class names in strings and resource files
-adaptclassstrings
-adaptresourcefilecontents **.xml,**.json

# Remove unused code aggressively
-dontwarn kotlin.**
-dontwarn androidx.**
-dontwarn io.ktor.**
-dontwarn com.google.android.gms.**

# Rename source files for obfuscation
-renamesourcefileattribute SourceFile

# Optional: keep line numbers for crash logs (remove for max obfuscation)
-keepattributes SourceFile,LineNumberTable

# Print full configuration to inspect rules
-printconfiguration ../build/tmp/full-r8-config.txt








# ---------------------------------------------------------ROOM---------------------------------------------------------
-keep class * extends androidx.room.RoomDatabase { <init>(); }

# ---------------------------------------------------------KOIN---------------------------------------------------------
# Keep annotation definitions
-keep class org.koin.core.annotation.** { *; }

# Keep classes annotated with Koin annotations
-keep @org.koin.core.annotation.* class * { *; }
