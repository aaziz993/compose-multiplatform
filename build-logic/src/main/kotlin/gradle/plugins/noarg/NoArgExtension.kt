package gradle.plugins.noarg

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension

public val Project.noArg: NoArgExtension get() = the()

public fun Project.noArg(configure: NoArgExtension.() -> Unit): Unit = extensions.configure(configure)
