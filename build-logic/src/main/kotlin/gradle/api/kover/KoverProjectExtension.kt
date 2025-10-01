package gradle.api.kover

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.kover: KoverProjectExtension get() = the()

public fun Project.kover(configure: KoverProjectExtension.() -> Unit): Unit = extensions.configure(configure)
