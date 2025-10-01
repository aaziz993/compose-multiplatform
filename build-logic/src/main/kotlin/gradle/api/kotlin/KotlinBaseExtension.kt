package gradle.api.kotlin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

public val Project.kotlin: KotlinBaseExtension get() = the()

public fun Project.kotlin(configure: KotlinBaseExtension.() -> Unit): Unit =
    extensions.configure(configure)
