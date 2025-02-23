package plugin.project.apple

import gradle.cocoapods
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureCocoapodsExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("cocoapods").id) {
        projectProperties.kotlin.cocoapods.let { cocoapods ->
            kotlin {
                cocoapods {
                    cocoapods.applyTo(this)
                }
            }
        }
    }
