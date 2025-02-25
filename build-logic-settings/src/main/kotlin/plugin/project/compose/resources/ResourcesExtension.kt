@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose.resources

import gradle.compose
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.resources
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureResourcesExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        projectProperties.compose.resources.let { resources ->
            resources.applyTo(compose.resources)
        }
    }
