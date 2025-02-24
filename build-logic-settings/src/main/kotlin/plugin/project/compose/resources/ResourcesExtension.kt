@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose.resources

import gradle.all
import gradle.compose
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.resources
import gradle.settings
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import plugin.project.model.Layout

internal fun Project.configureResourcesExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        projectProperties.compose.resources.let { resources ->
            resources.applyTo(compose.resources)
        }
    }
