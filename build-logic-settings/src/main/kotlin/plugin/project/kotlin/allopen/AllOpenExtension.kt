package plugin.project.kotlin.allopen

import gradle.allOpen
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureAllOpenExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("allopen").id) {
       projectProperties.plugins.allOpen.let { allOpen ->
            allOpen(allOpen::applyTo)
        }
    }
