package gradle.api.kotlin.web.npm

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmExtension

public val Project.npm: NpmExtension get() = the()

public fun Project.npm(configure: NpmExtension.() -> Unit): Unit = extensions.configure(configure)
