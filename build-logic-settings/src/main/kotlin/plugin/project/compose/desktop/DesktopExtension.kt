package plugin.project.compose.desktop

import gradle.compose
import gradle.desktop
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureDesktopExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        projectProperties.compose.desktop.applyTo(compose.desktop)
    }
