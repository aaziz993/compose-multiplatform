package plugin.project.kotlin.apollo

import gradle.apollo
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureApolloExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("apollo3").id) {
        projectProperties.plugins.apollo.applyTo(apollo)
    }
