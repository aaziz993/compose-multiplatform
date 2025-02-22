package plugin.project.kotlin.allopen

import gradle.allOpen
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureAllOpenExtension() =
    pluginManager.withPlugin(libs.plugins.allopen.get().pluginId) {
       settings.projectProperties.plugins.allOpen.let { allOpen ->
            allOpen(allOpen::applyTo)
        }
    }
