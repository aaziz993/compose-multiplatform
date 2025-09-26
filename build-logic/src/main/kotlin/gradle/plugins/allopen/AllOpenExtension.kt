package gradle.plugins.allopen

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

public val Project.allOpen: AllOpenExtension get() = the()

public fun Project.allOpen(configure: AllOpenExtension.() -> Unit): Unit = extensions.configure(configure)
