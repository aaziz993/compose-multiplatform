package plugin.project.gradle.buildconfig

import gradle.buildConfig
import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.gradle.buildconfig.model.BuildConfigSettings

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.buildConfig
                .takeIf { it.enabled && projectProperties.kotlin.hasTargets }?.let { buildConfig ->
                    plugins.apply(settings.libs.plugins.plugin("build.config").id)

                    buildConfig.applyTo()
                }
        }
    }
}
