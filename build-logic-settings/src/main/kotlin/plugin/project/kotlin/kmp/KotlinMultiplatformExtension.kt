package plugin.project.kotlin.kmp

import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKotlinMultiplatformExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id) {
        projectProperties.kotlin.applyTo(kotlin)
    }
