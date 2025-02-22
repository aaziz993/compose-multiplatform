package plugin.project.gradle.buildconfig

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class BuildConfigPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (!projectProperties.plugins.buildConfig.enabled ||projectProperties.kotlin.targets.isEmpty()) {
                return@with
            }

            plugins.apply(project.libs.plugins.build.config.get().pluginId)

            configureBuildConfigExtension()
        }
    }
}
