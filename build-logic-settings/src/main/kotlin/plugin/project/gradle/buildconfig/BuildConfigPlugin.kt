package plugin.project.gradle.buildconfig

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.buildConfig
                .takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { buildConfig ->
                    plugins.apply(settings.libs.plugins.plugin("build.config").id)

                    buildConfig.applyTo()
                }
        }
    }
}
