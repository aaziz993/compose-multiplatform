package plugin.project.gradle.kover

import gradle.kover
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureKoverExtension() =
    pluginManager.withPlugin(libs.plugins.kover.get().pluginId) {
       settings.projectProperties.plugins.kover.let { kover ->
            kover {
                kover.applyTo(this)
            }
        }
    }


