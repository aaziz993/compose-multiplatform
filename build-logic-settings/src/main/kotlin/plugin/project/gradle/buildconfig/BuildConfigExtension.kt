package plugin.project.gradle.buildconfig

import com.github.gmazzo.gradle.plugins.BuildConfigPlugin
import gradle.moduleProperties
import gradle.buildConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

internal fun Project.configureBuildConfigExtension() =
    plugins.withType<BuildConfigPlugin> {
        moduleProperties.settings.gradle.buildConfig.let { buildConfig ->
            buildConfig {
                buildConfig.sourceSets?.forEach(sourceSets::register)
            }
        }
    }
