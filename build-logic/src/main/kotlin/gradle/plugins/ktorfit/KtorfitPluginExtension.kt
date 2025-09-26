package gradle.plugins.ktorfit

import de.jensklingenberg.ktorfit.gradle.KtorfitPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.ktorfit: KtorfitPluginExtension get() = the()

public fun Project.ktorfit(configure: KtorfitPluginExtension.() -> Unit): Unit = extensions.configure(configure)
