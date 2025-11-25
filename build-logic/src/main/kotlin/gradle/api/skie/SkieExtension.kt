package gradle.api.skie

import co.touchlab.skie.plugin.configuration.SkieExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.skie: SkieExtension get() = the()

public fun Project.skie(configure: SkieExtension.() -> Unit): Unit = extensions.configure(configure)
