package gradle.plugins.powerassert

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

public val Project.powerAssert: PowerAssertGradleExtension get() = the()

public fun Project.powerAssert(configure: PowerAssertGradleExtension.() -> Unit): Unit =
    extensions.configure(configure)
