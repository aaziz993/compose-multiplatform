package plugin.project.cocoapods

import gradle.cocoapods
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureCocoapodsExtension() =
    pluginManager.withPlugin(libs.plugins.cocoapods.get().pluginId) {
       settings.projectProperties.kotlin.cocoapods.let { cocoapods ->
            cocoapods {
                cocoapods.applyTo(this)
            }
        }
    }
