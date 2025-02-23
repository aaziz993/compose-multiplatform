package plugin.project.gradle.spotless

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.spotless
import org.gradle.api.Project

private const val LICENSE_HEADER_DIR = "../"

internal fun Project.configureSpotlessExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("spotless").id) {
       projectProperties.plugins.spotless.let { spotless ->
            spotless {
                spotless.applyTo(this)
            }
        }
    }



