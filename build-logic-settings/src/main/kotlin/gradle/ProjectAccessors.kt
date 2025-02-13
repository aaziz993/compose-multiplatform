package gradle

import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure("kotlin", configure)

internal val Project.kotlin: KotlinMultiplatformExtension get() = the()

internal val Project.settings: Settings
    get() = (gradle as GradleInternal).settings

