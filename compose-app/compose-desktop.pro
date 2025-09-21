# =========================================
# Compose Desktop + Kotlin MPP ProGuard Rules
# =========================================

# ---------------------------
# Basic settings
# ---------------------------
-dontoptimize
-dontwarn androidx.compose.desktop.**
-dontwarn kotlinx.datetime.**
-dontwarn org.jetbrains.skiko.**
-dontwarn io.ktor.**
-dontwarn kotlinx.coroutines.**

# Keep Kotlin metadata and built-ins
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }

# Keep entry point
-keep class MainKt { *; }

# ---------------------------
# Compose & Skiko
# ---------------------------
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.skiko.** { *; }

# ---------------------------
# Ktor client & coroutines
# ---------------------------
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Keep volatile fields in Ktor for concurrency
-keepclassmembers class io.ktor.** { volatile <fields>; }

# ---------------------------
# Skiko / Desktop platform interop
# ---------------------------
-keep class sun.misc.Unsafe { *; }
-dontnote sun.misc.Unsafe

-keep class com.sun.jna** { *; }
-dontnote com.sun.jna**

-keep class com.jetbrains.JBR* { *; }
-dontnote com.jetbrains.JBR*

# ---------------------------
# Optional: token/highlighting libraries (if used)
# ---------------------------
-keep class dev.romainguy.kotlin.explorer.code.*TokenMarker { *; }
-dontnote dev.romainguy.kotlin.explorer.code.*TokenMarker

-keep class org.fife.** { *; }
-dontnote org.fife.**

# ---------------------------
# General guidance
# ---------------------------
-dontobfuscate # only remove if confident all entry points & runtime classes are kept
-printconfiguration build/tmp/full-r8-config.txt
