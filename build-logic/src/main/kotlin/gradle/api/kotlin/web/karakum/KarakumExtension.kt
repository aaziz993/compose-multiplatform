package gradle.api.kotlin.web.karakum

import io.github.sgrishchenko.karakum.gradle.plugin.KarakumExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.karakum: KarakumExtension get() = the()

public fun Project.karakum(configure: KarakumExtension.() -> Unit): Unit = extensions.configure(configure)
