package gradle.plugins.buildconfig

import com.github.gmazzo.buildconfig.BuildConfigExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.buildConfig: BuildConfigExtension get() = the()

public fun Project.buildConfig(configure: BuildConfigExtension.() -> Unit): Unit =
    extensions.configure(configure)
