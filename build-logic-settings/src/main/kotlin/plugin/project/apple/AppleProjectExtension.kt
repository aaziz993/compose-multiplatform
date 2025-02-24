package plugin.project.apple

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project
import org.jetbrains.gradle.apple.apple

internal fun Project.configureAppleProjectExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("apple").id) {
        projectProperties.apple.applyTo(apple)
    }
