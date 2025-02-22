package plugin.project.kotlin.apollo

import gradle.apollo
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureApolloExtension() =
    pluginManager.withPlugin(libs.plugins.apollo3.get().pluginId) {
       projectProperties.plugins.apollo.let { apollo ->
            apollo {
                apollo.applyTo(this)
            }
        }
    }
