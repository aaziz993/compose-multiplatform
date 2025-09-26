package gradle.plugins.dokka

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.dokka.versioning.VersioningConfiguration

public val Project.versioning: VersioningConfiguration get() = the()

public fun Project.versioning(configure: VersioningConfiguration.() -> Unit): Unit =
    extensions.configure(configure)
