package plugin.project.apple

import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.gradle.apple.ApplePlugin
import org.jetbrains.gradle.apple.apple

internal fun Project.configureAppleProjectExtension() =
    plugins.withType<ApplePlugin> {
       projectProperties.settings.apple.let { apple ->
            apple {
                apple.applyTo(this)
            }
        }
    }
