package plugin.project.gradle.buildconfig

import gradle.buildConfig
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureBuildConfigExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("build.config").id) {
       projectProperties.plugins.buildConfig.let { buildConfig ->
            buildConfig {
                buildConfig.applyTo(this)
            }
        }
    }
