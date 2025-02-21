package plugin.project.cocoapods

import gradle.cocoapods
import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project

internal fun Project.configureCocoapodsExtension() =
    pluginManager.withPlugin(libs.plugins.cocoapods.get().pluginId) {
        moduleProperties.settings.cocoapods.let { cocoapods ->
            cocoapods {
                cocoapods.applyTo(this)
            }
        }
    }
