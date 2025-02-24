package plugin.project.gradle.develocity

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.develocity

internal fun Settings.configureDevelocityConfiguration() =
    pluginManager.withPlugin(libs.plugins.plugin("develocity").id) {
        projectProperties.plugins.develocity.applyTo(develocity)
    }



