package plugin.project.android

import gradle.cocoapods
import gradle.libs
import gradle.projectProperties
import org.gradle.api.Project

internal fun Project.configureCocoapodsExtension() =
    pluginManager.withPlugin(libs.plugins.cocoapods.get().pluginId) {
       projectProperties.kotlin.cocoapods.let { cocoapods ->
            cocoapods {
                cocoapods.applyTo(this)
            }
        }
    }
