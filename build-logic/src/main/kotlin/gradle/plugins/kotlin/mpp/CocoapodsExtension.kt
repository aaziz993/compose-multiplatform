package gradle.plugins.kotlin.mpp

import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

public val KotlinMultiplatformExtension.cocoapods: CocoapodsExtension get() = the()

public fun KotlinMultiplatformExtension.cocoapods(configure: CocoapodsExtension.() -> Unit): Unit =
    extensions.configure(configure)
