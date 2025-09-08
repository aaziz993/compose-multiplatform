package gradle.plugins.kotlin.targets

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

public fun String.toJvmTarget(): JvmTarget = JvmTarget.fromTarget(if (this == "8") "1.8" else this)
