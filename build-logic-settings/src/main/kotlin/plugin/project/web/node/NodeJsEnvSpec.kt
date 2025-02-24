package plugin.project.web.node

import gradle.id
import gradle.libs
import gradle.nodeEnv
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureNodeJsEnvSpec() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("gradle.node.plugin").id) {
        projectProperties.nodeJsEnv.applyTo(nodeEnv)
    }
