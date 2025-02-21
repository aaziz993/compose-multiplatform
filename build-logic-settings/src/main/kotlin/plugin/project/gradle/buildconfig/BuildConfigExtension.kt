package plugin.project.gradle.buildconfig

import com.github.gmazzo.gradle.plugins.BuildConfigPlugin
import gradle.buildConfig
import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureBuildConfigExtension() =
    pluginManager.withPlugin(libs.plugins.build.config.get().pluginId) {
        moduleProperties.settings.gradle.buildConfig.let { buildConfig ->
            buildConfig {
                buildConfig.applyTo(this)
            }
        }
    }
