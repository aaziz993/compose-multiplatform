package gradle.plugins.publish

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.publishing: PublishingExtension get() = the()

public fun Project.publishing(configure: PublishingExtension.() -> Unit): Unit = extensions.configure(configure)
