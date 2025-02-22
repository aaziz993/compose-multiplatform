package plugin.project.gradle.buildconfig

import gradle.buildConfig
import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Project

internal fun Project.configureBuildConfigExtension() =
    pluginManager.withPlugin(libs.plugins.build.config.get().pluginId) {
       settings.projectProperties.plugins.buildConfig.let { buildConfig ->
            buildConfig {
                buildConfig.applyTo(this)
            }
        }
    }
