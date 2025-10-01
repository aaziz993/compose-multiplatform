package gradle.api.ktor

import io.ktor.plugin.features.KtorExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.ktor: KtorExtension get() = the()

public fun Project.ktor(configure: KtorExtension.() -> Unit): Unit = extensions.configure(configure)
