package gradle.api.dependencycheck

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

public val Project.dependencyCheck: DependencyCheckExtension get() = the()

public fun Project.dependencyCheck(configure: DependencyCheckExtension.() -> Unit): Unit =
    extensions.configure(configure)
