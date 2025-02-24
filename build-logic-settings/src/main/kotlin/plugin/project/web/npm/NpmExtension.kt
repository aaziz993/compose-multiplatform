package plugin.project.web.npm

import gradle.id
import gradle.libs
import gradle.npm
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.tryAssign
import org.gradle.api.Project

internal fun Project.configureNpmExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
       projectProperties.npm.applyTo(npm)
    }
