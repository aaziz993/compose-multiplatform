package plugin.project.apple

import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.gradle.apple.ApplePlugin
import org.jetbrains.gradle.apple.apple
import plugin.project.apple.model.AppleBuildSettings
import plugin.project.apple.model.BuildConfiguration

internal fun Project.configureAppleProjectExtension() =
    plugins.withType<ApplePlugin> {
        moduleProperties.settings.apple.let { apple ->
            apple {
                apple.applyTo(this)
            }
        }
    }
