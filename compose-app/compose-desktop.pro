# ===================================================
# 1. Compose Desktop & Skiko Libraries (keep intact)
# ===================================================
-keep class androidx.compose.** { *; }
-keep class androidx.ui.** { *; }
-keep class org.jetbrains.skiko.** { *; }
-keep class com.jetbrains.JBR* { *; }

# Desktop theme / styling / KeyEvents
-dontwarn androidx.compose.desktop.DesktopTheme*
-dontwarn androidx.compose.ui.input.key.KeyEvent_*
-dontnote androidx.compose.ui.input.key.KeyEvent_*

# ===================================================
# 2. Kotlin / Coroutines / Serialization / Reflection
# ===================================================
# Kotlin Metadata
-keepclassmembers class kotlin.Metadata { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }

# Serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { *; }

# ===================================================
# 3. Ktor & Networking
# ===================================================
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-keep class io.ktor.client.engine.cio.** { *; }

# Warnings suppression
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**

# ===================================================
# 4. JetBrains / Fife / JNA
# ===================================================
-keep class org.fife.** { *; }

# Unsafe – optional, suppress warnings
-dontwarn sun.misc.**
-dontnote sun.misc.**

-keep class com.sun.jna** { *; }
-dontnote com.sun.jna**

# ===================================================
# 5. TokenMarker & Markdown Extensions
# ===================================================
-keep class dev.romainguy.kotlin.explorer.code.*TokenMarker { *; }
-dontnote dev.romainguy.kotlin.explorer.code.*TokenMarker
-dontnote org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.**
-dontwarn org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.**

# ===================================================
# 6. Application code – obfuscate & shrink
# ===================================================
# Keep libraries intact, obfuscate only your app
# Replace "com.example.myapp" with your actual package name
-keep class io.github.aaziz993.composeapp.** { *; }

# Adapt class names in strings and resource files
-adaptclassstrings
-adaptresourcefilecontents **.xml,**.json

# ===================================================
# 7. Debug / Crash Reporting
# ===================================================
# Keep line numbers for crash logs
-keepattributes SourceFile,LineNumberTable

# ===================================================
# 8. Optional: print config
# ===================================================
-printconfiguration ../build/tmp/full-r8-config-desktop.txt
