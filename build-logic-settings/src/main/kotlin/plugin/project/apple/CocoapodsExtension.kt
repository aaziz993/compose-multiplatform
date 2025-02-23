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
import org.gradle.kotlin.dsl.withType
import org.jetbrains.gradle.apple.ApplePlugin
import org.jetbrains.gradle.apple.apple
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin

internal fun Project.configureCocoapodsExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("cocoapods").id) {
        projectProperties.settings.apple.cocoapods.let { cocoapods ->
            kotlin {
                cocoapods {
                    cocoapods.applyTo(this)
                }
            }
        }
    }
