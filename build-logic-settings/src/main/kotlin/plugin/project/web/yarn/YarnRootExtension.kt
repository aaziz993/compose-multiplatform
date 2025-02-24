package plugin.project.web.yarn

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.yarn
import org.gradle.api.Project

internal fun Project.configureYarnRootExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
        projectProperties.yarn.applyTo(yarn)
    }
