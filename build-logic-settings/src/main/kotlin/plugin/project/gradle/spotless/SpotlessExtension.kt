package plugin.project.gradle.spotless

import gradle.libs
import gradle.projectProperties
import gradle.settings
import gradle.spotless
import org.gradle.api.Project

private const val LICENSE_HEADER_DIR = "../"

internal fun Project.configureSpotlessExtension() =
    pluginManager.withPlugin(libs.plugins.spotless.get().pluginId) {
       settings.projectProperties.plugins.spotless.let { spotless ->
            spotless {
                spotless.applyTo(this)
            }
        }
    }



