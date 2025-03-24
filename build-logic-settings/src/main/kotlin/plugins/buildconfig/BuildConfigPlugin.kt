package plugins.buildconfig

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.buildConfig
                .takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { buildConfig ->
                    plugins.apply(project.settings.libs.plugins.plugin("buildConfig").id)

                    buildConfig.applyTo()
                }
        }
    }
}
