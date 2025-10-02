package gradle.api.powerassert

import gradle.api.kotlin.mpp.kotlin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

public val Project.powerAssert: PowerAssertGradleExtension get() = the()

public fun Project.powerAssert(configure: PowerAssertGradleExtension.() -> Unit): Unit =
    extensions.configure(configure)

public fun Project.includeKotlinSourceSets(): Unit =
    pluginManager.withPlugin("org.jetbrains.kotlin.plugin.power-assert") {
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            powerAssert.includedSourceSets = kotlin.sourceSets.map(KotlinSourceSet::getName)
        }
    }
