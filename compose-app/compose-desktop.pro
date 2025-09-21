# ===========================
# Compose Desktop + Kotlin MPP
# ===========================

# General
-dontoptimize
-dontwarn androidx.compose.desktop.**
-dontwarn kotlinx.datetime.**
-dontwarn org.jetbrains.skiko.**
-dontwarn io.ktor.**
-dontwarn kotlinx.coroutines.**

# Keep Kotlin metadata & built-ins
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }

# Keep entry point
-keep class MainKt { *; }

# Compose & Skiko
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.skiko.** { *; }

# Ktor & coroutines
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }

# Platform / Desktop interop
-keep class sun.misc.Unsafe { *; }
-dontnote sun.misc.Unsafe

-keep class com.sun.jna** { *; }
-dontnote com.sun.jna**

-keep class com.jetbrains.JBR* { *; }
-dontnote com.jetbrains.JBR*

# Optional: syntax/highlighting libs
-keep class dev.romainguy.kotlin.explorer.code.*TokenMarker { *; }
-dontnote dev.romainguy.kotlin.explorer.code.*TokenMarker

-keep class org.fife.** { *; }
-dontnote org.fife.**

# Print final config for debug
-printconfiguration build/tmp/full-r8-config.txt
